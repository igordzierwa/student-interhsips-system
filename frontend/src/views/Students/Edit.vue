<template>
  <el-row>
    <el-col :span="24">
      <div class="grid-content bg-purple-dark">
        <h3>Edytuj dane studenta</h3>

        <el-form
          ref="StudentEditForm"
          :rules="rules"
          :model="model"
          v-loading="loading"
        >
          <div>
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
              <el-button type="success" @click="handleSubmit">Zapisz</el-button>
            </el-row>
          </div>
        </el-form>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "StudentEdit",
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
    return {
      componentName: "StudentEditForm",
      editingId: -1,
      model: {
        username: "",
        name: "",
        surname: "",
        password: "",
        confirmPassword: "",
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
    handleSubmit() {
      // if (!this.valid(this.componentName)) return false;

      var data = {
        username: this.model.username,
        name: this.model.name,
        surname: this.model.surname,
        password: this.model.password,
        confirmPassword: this.model.confirmPassword,
        email: this.model.email,
      };

      var url = this.api + "/students/" + this.editingId;

      this.axios({
        method: "POST",
        url: url,
        data: data,
      }).then((resp) => {
        console.log(resp);
        this.$message({
          message: "Operacja wykonana poprawnie",
          type: "success",
        });
      });
    },
    loadEntity() {
      this.axios({
        method: "GET",
        url: this.api + "/students/" + this.editingId,
      }).then((resp) => {
        console.log(resp);
        if (resp.data) {
          this.model.name = resp.data.name;
          this.model.short_name = resp.data.shortname;
        } else {
          this.$message({
            message: "Brak studenta o ID = " + this.editingId,
            type: "error",
          });
          this.$router.push("/students");
        }
      });
    },
  },
  mounted() {
    this.editingId = this.$route.params.id;
    this.loadEntity(this.editingId);
  },
};
</script>

<style scoped></style>
