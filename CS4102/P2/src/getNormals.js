function getNormals(vertices, verticesPerFace) {
  var normals = [];
  for (let i = 0; i < indices.length; i++) {
    var face = indices[i];

    var p0 = vec3.fromValues(...vertices[face[0]].slice(0, 3))
    var p1 = vec3.fromValues(...vertices[face[1]].slice(0, 3))
    var p2 = vec3.fromValues(...vertices[face[2]].slice(0, 3))

    var v0 = vec3.create();
    var v1 = vec3.create();

    vec3.subtract(v0, p0, p1);
    vec3.subtract(v1, p1, p2);

    var normal = vec3.create();
    vec3.cross(normal, v0, v1);

    vec3.normalize(normal, normal);

    var normalArray = [...Array.from(normal), 0]

    // Each face is made of 3 vertices with the same normals
    for (let i = 0; i < verticesPerFace; i++) {
      normals.push(normalArray)
    }
  }
}