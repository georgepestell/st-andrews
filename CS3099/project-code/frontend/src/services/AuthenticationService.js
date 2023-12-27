import Api from "@/services/Api.js";

export default {
  async register(email, password, displayName, role) {
    try {
      var res = await Api().post("/auth/register", {
        email: email,
        password: password,
        displayName: displayName,
        role: role
      })

      if (res.data) {
        console.log(res.data);
        alert(res.data.message);
        window.location.href = "/auth/authorize";
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

  /**
   * Allows users registered with G24 to login
   * @param email The users registered email address
   * @param password The registered users plaintext password 
   * @param redirect The URL to redirect to once logged in - '/' for local, or a registered federation url
   * 
   */
  async login(email, password, redirect) {
    try {
      // Send login request to the backend
      var res = await Api().post("/auth/login", {
        email: email,
        password: password,
        redirect: redirect
      })

      if (res.status == 200) {

        var token = res.data.token ? res.data.token : "";

        // Log the user if local redirect
        if (redirect == "/") {
          $cookies.set("user", res.data.user);
        }

        window.location.href = redirect + token;


      }

    } catch (err) {
  
      // Alert the user to the error
      if (err.response) {
        alert("Error: " + err.response.data.message);
      } else {
        console.log(err);
      } 

    }
  },

  async logout() {
    var response = await Api().post("/auth/logout", null)
    if (response.status == 200) {
      $cookies.remove("user");
      window.location.href = '/'
    }
    return
  },

  isLoggedIn() {
    return $cookies.get("user") != null;
  }
}
