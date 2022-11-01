<template>
  <el-row>
    <el-col :span="24">
      <div class="grid-content bg-purple-dark">
        <h3>Dodaj grupę</h3>

        <el-form ref="SGAddForm" :rules="rules" :model="model">
          <div>
            <el-form-item label="Nazwa grupy:" prop="name">
              <el-input v-model="model.name"></el-input>
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
  name: "AddStudentGroup",
  data() {
    let validateStudentGroupName = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Pole wymagane"));
      } else {
        this.checkStudentGroupNameAvailable(() => {
          if (this.groupNameAvailable) {
            callback();
          } else {
            callback(new Error("Nazwa grupy zajęta"));
          }
        });
      }
    };
    return {
      groupNameAvailable: "",
      userRole: "",
      universitySupervisorId: "",
      model: {
        name: "",
      },
      rules: {
        name: [
          {
            required: true,
            validator: validateStudentGroupName,
            trigger: "blur",
          },
        ],
      },
      componentName: "SGAddForm",
    };
  },
  methods: {
    async handleSubmit() {
      let valid;
      await this.validateForm(this.componentName).then((resp) => {
        valid = resp;
      });
      if (!valid) {
        this.$message({
          message: "Niepoprawne dane",
          type: "error",
        });
        return false;
      }

      this.axios({
        method: "POST",
        url: this.api + "/studentGroups",
        data: {
          studentGroupName: this.model.name,
          universitySupervisorId: this.universitySupervisorId,
        },
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((resp) => {
          console.log(resp);
          this.$message({
            message: "Dane zostały zapisane.",
            type: "success",
          });
          this.$router.push("/admin/student-groups/list");
        })
        .catch(() => {
          this.$message({
            message: "Błąd komunikacji z serwerem.",
            type: "error",
          });
        });
    },
    checkStudentGroupNameAvailable: function(method) {
      this.axios
        .get(this.api + "/studentGroups/check/" + this.model.name)
        .then((resp) => {
          this.groupNameAvailable = resp.data === "true";
          method();
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getUserData() {
      this.axios
        .get(this.api + "/logged/session")
        .then((resp) => {
          const roles = resp.data["roles"];
          if (roles.includes("university_supervisor")) {
            this.userRole = "university_supervisor";
            this.universitySupervisorId = resp.data["id"];
          } else if (roles.includes("admin")) {
            this.userRole = "admin";
          }
        })
        .catch(() => {
          console.info("User not logged...");
          this.$router.push("/");
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
    this.getUserData();
  },
};
</script>

<style scoped></style>
