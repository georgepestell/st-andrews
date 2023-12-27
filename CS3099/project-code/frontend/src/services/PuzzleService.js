import Api from "@/services/Api.js";

export default {
  async getPuzzle(puzzleID) {
    const res = await Api().get("/puzzles/" + puzzleID);
    return res.data.puzzle;
  },
  async solve(solution) {
    return await Api().post("/puzzles/solve", solution);
  },
  async getPuzzles(limit, page) {
    try {
      const res = await Api().get("/puzzles?limit=" + limit + "&page=" + page);

      if (res.status == 200) {
        return res.data.puzzles;
      } else {
        throw Error("Invalid return value");
      }
    } catch (err) {
      if (err.response) {
        alert("Error: " + err.response.data.message);
        return;
      } else {
        console.log(err);
      }
    }
  },
  async create(puzzleType, values, solution, inputs, diagonalWord) {
    Api()
      .post("/puzzles/create", {
        puzzleType: puzzleType,
        values: values,
        solution: solution,
        inputs: inputs,
        diagonalWord: diagonalWord,
      })
      .then(
        (response) => {
          if (response.status == 200) {
            alert(
              "Puzzle Created Successfully.\nYou will now be redirected to our home page!"
            );
            window.location.href = "/";
            return;
          }
        },
        (err) => {
          if (err.response) {
            if (!err.response.data.stay) {
              alert("Error: " + err.response.data.message);
              return;
            } else {
              alert(
                "Error: " +
                  err.response.data.message +
                  "\nYou will now be redirected to our home page!"
              );
              window.location.href = "/";
              return;
            }
          } else {
            console.log(err);
          }
        }
      );
  },
  async getRandom() {
    const res = await Api().get("/puzzles/random");
    if (res.data) {
      return res.data.puzzle;
    }
  },
  async getComments(puzzleID, limit, page) {
    const res = await Api().get(
      "/comments/puzzles/" + puzzleID + "?limit=" + limit + "&page=" + page
    );
    if (res.data.comments) {
      return res.data.comments;
    }
  },
  async postComment(puzzleID, content) {
    const res = await Api().post("/comments/puzzles/" + puzzleID, {
      comment: content,
    });
    return res.status == 200;
  },
};
