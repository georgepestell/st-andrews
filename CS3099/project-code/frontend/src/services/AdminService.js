import Api from "@/services/Api.js";

export default {
  async getUsers(pageSize, pageNo) {
    return Api().get("/manage/user/list", {params: {
      pageSize: pageSize,
      pageNo: pageNo
    }});
  },
  async removeUser(id) {
    return Api().post("/manage/user/remove", {removeID: id});
  },
  async updateRole(id, newRole) {
    return Api().post('/manage/user/updaterole',{
      _id: id,
      newRole: newRole
    })
  },
  async removePuzzle(id){
    return Api().post('/manage/puzzles/remove', {removeID: id});
  },
  async getPuzzleSolution(id){
    return Api().get('/manage/puzzles/solution', {params:{
      puzzleID: id
    }});
  },
  // FOR PERSONAL USER UPDATE
  async updateUser(updates) {
    // user id fetched in backend
    return Api().post('/manage/user/updatepersonal',{
      updates: updates 
    })
  }
};
