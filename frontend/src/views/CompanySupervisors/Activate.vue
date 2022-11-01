<template>
  <div class="hello">
    <el-row type="flex" class="row-bg" justify="space-around">
      <el-col :span="18">
        <h4>
          Wpisz login, hasło oraz e-mail na który został przysłany token
          aktywacyjny
        </h4>
      </el-col>
    </el-row>
    <el-row type="flex" class="row-bg" justify="space-around">
      <el-col :span="18">
        <el-form
          v-on:keyup.enter="handleSubmit"
          ref="activateUCForm"
          :rules="rules"
          :model="model"
          v-loading="loading"
        >
          <div class="notlogged" v-if="!userLogged">
            <el-form-item label="Login:" prop="login">
              <el-input v-model="model.login" max="30"></el-input>
            </el-form-item>
            <el-form-item label="Hasło:" prop="password">
              <el-input type="password" v-model="model.password"></el-input>
            </el-form-item>
            <el-form-item label="Powtórz hasło:" prop="confirmPassword">
              <el-input
                type="password"
                v-model="model.confirmPassword"
              ></el-input>
            </el-form-item>
            <el-form-item label="Adres e-mail:" prop="email">
              <el-input v-model="model.email"></el-input>
            </el-form-item>
            <el-row>
              <div class="register">
                <el-button @click="handleSubmit">Zarejestruj się</el-button>
              </div>
            </el-row>
          </div>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "ActivateCompanySupervisor",
  data() {
    let validateLogin = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Pole wymagane"));
      } else {
        this.checkUsernameAvailable(() => {
          if (this.usernameAvailable) {
            callback();
          } else {
            callback(new Error("Nazwa użytkownika zajęta"));
          }
        });
      }
    };
    let validateEmail = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Pole wymagane"));
      } else if (!this.validEmail(this.model.email)) {
        callback(new Error("Podaj e-mail w poprawnym formacie"));
      }
      callback();
    };
    let validatePasswords = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Pole wymagane"));
      } else {
        if (this.model.password !== this.model.confirmPassword) {
          callback(new Error("Hasła się nie zgadzają!"));
        } else {
          callback();
        }
      }
    };
    return {
      userLogged: false,
      componentName: "activateCSForm",
      usernameAvailable: false,
      emailAvailable: false,
      model: {
        login: "",
        password: "",
        confirmPassword: "",
        name: "",
        surname: "",
        email: "",
      },
      rules: {
        login: [
          { required: true, validator: validateLogin, trigger: "blur" },
          {
            min: 6,
            max: 20,
            message: "Min. długość loginu to 6, max. to 20",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "Pole wymagane", trigger: "blur" },
          {
            min: 8,
            max: 20,
            message: "Min. długość hasła to 8, max. to 20",
            trigger: "blur",
          },
        ],
        confirmPassword: [
          { required: true, validator: validatePasswords, trigger: "blur" },
          {
            min: 8,
            max: 20,
            message: "Min. długość hasła to 8, max. to 20",
            trigger: "blur",
          },
        ],
        email: [{ required: true, validator: validateEmail, trigger: "blur" }],
      },
    };
  },
  methods: {
    handleUserLogged() {
      this.userLogged = true;
      this.$router.push("/your-account/");
    },
    handleUserNotLogged() {
      this.userLogged = false;
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
        });
    },
    handleSubmit() {
      this.validateForm(this.componentName).then((resp) => {
        if (!resp) {
          this.$message({
            message: "Niepoprawne dane",
            type: "error",
          });
          return false;
        }
      });
      console.log(this.$route.query.token);

      this.axios({
        method: "POST",
        url: this.api + "/activate-company-supervisor",
        data: {
          username: this.model.login,
          password: this.model.password,
          confirmPassword: this.model.confirmPassword,
          email: this.model.email,
        },
        params: {
          token: this.$route.query.token,
        },
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((resp) => {
          console.log(resp);
          this.$message({
            message: "Dane zostały zapisane w systemie.",
            type: "success",
          });
          this.$router.push("/");
        })
        .catch((error) => {
          let message = "Błąd komunikacji z serwerem.";
          if (error.data) {
            message = error.data;
          }
          this.$message({
            message: message,
            type: "error",
          });
        });
    },
    checkUsernameAvailable: function(method) {
      this.axios
        .get(this.api + "/check/username/" + this.model.login)
        .then((resp) => {
          this.usernameAvailable = resp.data === "true";
          method();
        })
        .catch((error) => {
          console.log(error);
        });
    },
    validEmail: function(email) {
      let re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    },
    validateForm(formName) {
      return new Promise((resolve) => {
        this.$refs[formName].validate((valid) => {
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

<style scoped>
.hello {
  margin: auto;
}
</style>
