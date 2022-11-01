<template>
  <div id="youracc" class="full">
    <h3>Twoje dane</h3>
    <el-form>
      <el-row>
        <el-form ref="model" :rules="rules" :model="model">
          <admin :model="model" v-if="model.role == 'admin'"></admin>
          <university-superviser
            :model="model"
            v-if="model.role == 'university_superviser'"
          ></university-superviser>
          <company-superviser
            :model="model"
            v-if="model.role == 'company_superviser'"
          ></company-superviser>
          <student :model="model" v-if="model.role == 'student'"></student>
          <el-form-item label="Rola użytkownika" prop="role">
            <el-input v-model="model.role" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="Nazwa użytkownika" prop="username">
            <el-input v-model="model.username" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="Imię" prop="name">
            <el-input
              v-model="model.name"
              type="text"
              :disabled="true"
            ></el-input>
          </el-form-item>
          <el-form-item label="Nazwisko" prop="surname">
            <el-input
              v-model="model.surname"
              type="text"
              :disabled="true"
            ></el-input>
          </el-form-item>
          <el-form-item label="E-Mail" prop="email">
            <el-input v-model="model.email" :disabled="true"></el-input>
          </el-form-item>
          <el-button @click="handleLogoutClick">Wyloguj</el-button>
        </el-form>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import Admin from "./Admin";
import UniversitySupervisor from "./UniversitySupervisor";
import CompanySupervisor from "./CompanySupervisor";
import Student from "./Student";
export default {
  name: "YourAccount",
  components: {
    Student,
    CompanySuperviser: CompanySupervisor,
    UniversitySuperviser: UniversitySupervisor,
    Admin,
  },
  data() {
    return {
      loggedUserId: "",
      model: {
        username: "",
        name: "",
        surname: "",
        email: "",
        role: "",
      },
      rules: {},
    };
  },
  methods: {
    getUserById: function() {
      this.axios
        .get(this.api + "/logged/session", {
          headers: { "Content-Type": "application/json" },
        })
        .then((resp) => {
          console.log(resp);
          console.log(resp.data, this.model);
          this.model.username = resp.data.username;
          this.model.name = resp.data.name;
          this.model.surname = resp.data.surname;
          this.model.email = resp.data.email;
          this.model.role = resp.data.roles[0];
          this.$store.state.userData = resp.data;
        })
        .catch((error) => {
          console.log(error);
          this.loading = false;
          this.$message({
            message: "Błąd komunikacji z serwerem",
            type: "Error",
          });
        });
    },
    handleLogoutClick() {
      localStorage.removeItem("swops-bearer-token");
      this.$store.state.session.token = "";
      this.$store.state.userData = {};

      this.$router.push("/");
    },
  },
  mounted: function() {
    this.loggedUserId = localStorage.getItem("loggedID");
    this.getUserById();
  },
  computed: {},
};
</script>

<style scoped lang="scss">
  .el-row{
    width: 100%;
    .el-form{
      width: 100%;
    }
  }
#register {
  h1 {
    font-size: 14px;
  }
  h3 {
    text-align: center;
    margin: auto;
  }
}
#button {
  text-align: center;
  margin: auto;
}
</style>
