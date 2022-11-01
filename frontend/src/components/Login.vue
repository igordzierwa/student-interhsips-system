<template>
  <div class="hello" style="width: 100%;">
    <el-row type="flex" class="row-bg" justify="space-around">
      <el-col :span="18">
        <h4>
          Zaloguj się lub zarejestruj aby skorzystać :-)
        </h4>
      </el-col>
    </el-row>
    <el-row type="flex" class="row-bg" justify="space-around">
      <el-col :span="18">
        <el-form
          v-on:keyup.enter="handleSubmit"
          ref="LoginForm"
          :rules="rules"
          :model="model"
          v-loading="loading"
        >
          <div class="notlogged" v-if="!userLogged">
            <el-form-item label="Login:" prop="login">
              <el-input v-model="model.login"></el-input>
            </el-form-item>
            <el-form-item label="Hasło:" prop="password">
              <el-input
                type="password"
                v-model="model.password"
                max="9999"
              ></el-input>
            </el-form-item>
            <el-row>
              <el-button type="success" @click="handleSubmit"
                >Zaloguj</el-button
              >
            </el-row>
          </div>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "LoginForm",
  props: {
    msg: String,
  },
  data() {
    return {
      componentName: "LoginForm",
      mydata: [],
      loading: false,
      //wprowadzona zmienna potrzebna do rezerwacji
      userLoggedId: -1,
      userLogged: false,
      model: {
        login: "",
        password: "",
      },
      rules: {
        login: [
          { required: true, message: "Pole wymagane", trigger: "change" },
          //{ required: true, type: 'email', message: "Pole wymagane", trigger: 'change' }
        ],
        password: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    handleUserLogged() {
      this.userLogged = true;
      this.loading = false;
      //this.$router.addRoute("your-account")
      this.$router.push("/your-account/");
      //redirect to profile component
    },
    handleUserNotLogged() {
      this.userLogged = false;
      this.loading = false;
    },
    checkIfLogged() {
      console.log("checking logged user");
      this.axios
        .get(this.api + "/logged/session")
        .then(() => {
          this.handleUserLogged();
        })
        .catch(() => {
          this.handleUserNotLogged();
          //  this.$emit("loggedUnSuccessful", error);
        });
    },

    handleSubmit() {
      if (!this.valid(this.componentName)) return false;

      var data = new FormData();
      data.append("username", this.model.login);
      data.append("password", this.model.password);
      this.axios({
        method: "POST",
        url: this.api + "/auth",
        data: data,
      })
        .then((resp) => {
          console.log(resp);
          this.$message({
            message: "Zalogowano pomyślnie",
            type: "success",
          });
          this.$emit("loggedSuccessful", resp);
          this.userLogged = true;
          this.loading = false;

          this.$store.state.session.token = resp.data.token;
          localStorage.setItem("swops-bearer-token", resp.data.token);

          this.handleUserLogged();
        })
        .catch(() => {
          this.$message({
            message: "Błąd logowania. Wpisz poprawny login i hasło.",
            type: "error",
          });
        });
    },
    routeToRegister() {
      this.$router.push("/register");
    },
    validate() {
      return new Promise((resolve) => {
        this.$refs.Application.validate((valid) => {
          this.$emit("on-validate", valid, this.model);
          resolve(valid);
        });
      });
    },
  },
  mounted() {
    this.checkIfLogged();
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
