module.exports = {
    /**
     * method for creating a new role in the database
     * @param {*} client mongo client for the connection to database
     * @param {*} newRoleName name of the role
     * @param {*} newPerms permission value for checking role access
     */
    createRole: async function (client, newRoleName, newPerms) {
      const result = await client.db('userDB').collection('roles').insertOne({
        name: newRoleName,
        perms: newPerms
      })
      console.log('New role created with the following id: ' + result.insertedId)
    },
  
    /**
     * method for creating a new permission in the database
     * @param {*} client mongo client for connecting to database
     * @param {*} newPermName name of the permission
     * @param {*} newValue value for checking role access
     */
    createPermission: async function (client, newPermName, newValue) {
      const result = await client.db('userDB').collection('permissions').insertOne({
        name: newPermName,
        value: newValue
      })
  
      console.log('New permission created with the following id: ' + result.insertedId)
    },
  
    /**
     * method that checks if a certain role will have access to certain permission.
     * The perms in role entry will bitwise AND with the value of permission entry.
     * If the result is NOT 0, it means that such role will have access to the specified permission
     *
     * @param {*} client mongo client for connecting to database
     * @param {*} roleID the ID is used to get the permission entry from the collection
     * @param {*} permissionID the ID is used to get the role entry from the collection
     */
    checkRolePermission: async function (client, roleID, permissionID) {
      const role = await client.db('userDB').collection('roles').findOne({ _id: roleID })
      const permission = await client.db('userDB').collection('permissions').findOne({ _id: permissionID })
  
      console.log(role)
      console.log(permission)
  
      if ((role.perms & permission.value) !== 0) {
        console.log(role.name + ' has permission to ' + permission.name)
      } else {
        console.log(role.name + ' DOES NOT have permission to ' + permission.name)
      }
    },
  
    /**
     * method for listing out the whole roles collection and whole permission collection
     * @param {*} client mongo client for connecting to database
     */
    showAllRolesAndPermission: async function (client) {
      const rResult = await client.db('userDB').collection('roles').find().toArray()
      const pResult = await client.db('userDB').collection('permissions').find().toArray()
  
      console.log(rResult)
      console.log('\n' + pResult)
    }
  }
  