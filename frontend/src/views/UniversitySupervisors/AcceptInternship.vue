<template>
  <div class="full">
    <el-row >
      <el-col>
        <h4>
          Akceptacja praktyki dodanej przez studenta i zaakceptowanej przez opiekuna ze strony firmy
        </h4>

        <h5>Dane studenta</h5>
        <el-card class="box-card">
          <ul>
            <li>Imię studenta: {{model.downloaded.internship.student.name}}</li>
            <li>Nazwisko studenta: {{model.downloaded.internship.student.surname}}</li>
            <li>Email studenta: {{model.downloaded.internship.student.email}}</li>
            <li>Numer albumu: {{model.downloaded.internship.number_index}}</li>
            <li>Kierunek studiów: {{model.downloaded.internship.fieldOfStudy}}</li>
          </ul>
        </el-card>

        <h5>Dane o praktyce</h5>
        <el-card class="box-card">
          <ul>
            <li>Ilość godzin: {{model.downloaded.internship.hoursCount}}</li>
            <li>Data rozpoczęcia praktyk: {{model.downloaded.internship.dateStarted}}</li>
            <li>Data zakończenia: {{model.downloaded.internship.dateEnded}}</li>
            <li>Sprawozdanie: {{model.downloaded.internship.report}}</li>
            <li>Praktykant został oceniony na: {{model.downloaded.internship.gradesFromCompanySupervisor}}</li>
          </ul>
        </el-card>

      </el-col>
    </el-row>
    <el-row >
      <el-col>
        <el-form
          v-on:keyup.enter="handleSubmit"
          ref="acceptinternshipus"
          :rules="rules"
          :model="model"
          v-loading="loading"
        >
          <h5>Wypełnij dane:</h5>
          <el-form-item label="Ocena praktykanta:">
            <el-input-number v-model="model.grade" :min="2" :max="5"></el-input-number>

          </el-form-item>
        </el-form>
      </el-col>

    </el-row>
    <el-row>
      <div class="buttons">
        <el-button @click="handleSubmit">Zapisz i zaakceptuj praktykę</el-button>
      </div>
    </el-row>

  </div>
</template>

<script>
export default {
  name: "ActivateCompanySupervisor",
  data() {
    return {
      userLogged: false,
      componentName: "acceptinternshipus",
      usernameAvailable: false,
      emailAvailable: false,
      loading:'',
      model: {
        login: "",
        password: "",
        confirmPassword: "",
        name: "",
        surname: "",
        email: "",
        grade: 5,
        review: '',
        downloaded: {
          internship: {
            student: {}
          }
        }
      },
      rules: {
        grade: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        review: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
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
        } else {


      this.axios({
        method: "POST",
        url: this.api + "/accept-internship/university-supervisor",
        data: {
          grade: this.model.grade,
          token: this.$route.query.token
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
        }
      });
    },

    getInternshipData() {
      this.axios({
        method: "GET",
        url: this.api + "/accept-internship/supervisor",
        params: {
          token: this.$route.query.token
        }
      }).then((resp) => {
        this.model.downloaded = resp.data;

        console.log(this.model);
      });
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
    this.getInternshipData();
  },

};
</script>

<style scoped>
  .full{
    text-align: left;
  }
</style>
