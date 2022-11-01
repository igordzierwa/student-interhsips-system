import axios from "axios";
import store from "../store";
import VueRouter from "../router";
import app from "../main.js";

axios.defaults.withCredentials = false;

axios.interceptors.request.use(function(config) {
  try {
    const token = store.state.session.token;
    if (token == "undefined" || token == "") throw new Error("Not logged");

    if (token && token != undefined) {
      config.headers.Authorization = token;
    }

    console.log(token);
  } catch (e) {
    console.warn("You are not logged");
  }
  return config;
});

axios.interceptors.response.use(undefined, (err) => {
  err = err.response;
  var test = this;
  console.log(test);
  const whiteList = [
    "/register",
    "/university-supervisors/activate",
    "/company-supervisors/activate",
  ];
  return new Promise(() => {
    if (
      err.status === 401 ||
      (err.status === 403 && err.config && !err.config.__isRetryRequest)
    ) {
      // if you ever get an unauthorized, logout the user
      // this.$store.dispatch(AUTH_LOGOUT)
      if (!whiteList.includes(VueRouter.currentRoute.value.path)) {
        VueRouter.push("/");
      }
      // you can also redirect to /login if needed !
    } else if (err.status === 500) {
      app.$message({
        message: "Wystąpił błąd. Message: " + err.data.message,
        type: "error",
      });
    } else if (err.status === 400){
      app.$message({
        message: "Wystąpił błąd E400. Message: " + err.data.message,
        type: "error",
      });
      setTimeout(()=> {
        if(typeof err.data.details != "undefined" && err.data.details != null ){
          app.$message({
            message: err.data.details,
            type: "error",
          });
        }
      },100)

    }
    throw err;
  });
});

export default axios;
