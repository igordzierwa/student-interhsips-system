<template>
  <div class="full">
    <el-row type="flex" class="row-bg" justify="space-around">
      <el-col :span="18">
        <h4>Zarejestruj się!</h4>
      </el-col>
    </el-row>
    <el-row type="flex" class="row-bg" justify="space-around">
      <el-col :span="18">
        <el-form
          v-on:keyup.enter="handleSubmit"
          ref="RegisterForm"
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
            <el-form-item label="Imię:" prop="name">
              <el-input v-model="model.name"></el-input>
            </el-form-item>
            <el-form-item label="Nazwisko:" prop="surname">
              <el-input v-model="model.surname"></el-input>
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
  name: "RegisterForm",
  props: {
    msg: String,
  },
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
      } else {
        if (this.validEmail(this.model.email)) {
          this.checkEmailAvailable(() => {
            if (this.emailAvailable) {
              callback();
            } else {
              callback(new Error("Email jest już w systemie"));
            }
          });
        } else {
          callback(new Error("Podaj e-mail w poprawnym formacie"));
        }
      }
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
    return{
      usernameAvailable: false,
      emailAvailable: false,
      componentName: 'RegisterForm',
      mydata: [],
      loading: false,
      userLoggedId: -1,
      userLogged: false,
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
        name: [{ required: true, message: "Pole wymagane", trigger: "blur" }],
        surname: [
          { required: true, message: "Pole wymagane", trigger: "blur" },
        ],
        email: [{ required: true, validator: validateEmail, trigger: "blur" }],
      },
    };
  },
  methods: {
    handleUserLogged() {
      this.userLogged = true;
      this.loading = false;
      this.$router.push("/your-account/");
    },
    checkIfLogged() {
      console.log("checking logged user");
      this.axios
        .get(this.api + "/logged/session")
        .then(() => {
          console.log("OK!");
          this.handleUserLogged();
        })
        .catch(() => {
          console.info("User not logged, can be registered...");
        });
    },
    handleSubmit() {
      this.validateForm(this.componentName).then((resp) => {
        if(!resp){
          this.$message({
            message: "Niepoprawne dane",
            type: "error",
          });
          return false;
        } else {
          this.axios({
            method: "POST",
            url: this.api + "/registration",
            data: {
              username: this.model.login,
              password: this.model.password,
              confirmPassword: this.model.confirmPassword,
              name: this.model.name,
              surname: this.model.surname,
              email: this.model.email,
            },
            headers: {
              "Content-Type": "application/json",
            }
          })
              .then((resp) => {
                console.log(resp);
                this.$message({
                  message: "Rejestracja przebiegła pomyślnie.",
                  type: "success",
                });
              })
              .catch(() => {
                this.$message({
                  message: "Błąd przy rejestracji.",
                  type: "error",
                });
              });
        }
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
    checkEmailAvailable: function(method) {
      this.axios
        .get(this.api + "/check/email/" + this.model.email)
        .then((resp) => {
          this.emailAvailable = resp.data === "true";
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
    }
  },
  mounted() {
    this.checkIfLogged();
  },
};
</script>

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
