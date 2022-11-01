<template>
  <el-row>
    <el-col :span="24">
      <div class="grid-content bg-purple-dark">
        <h3 v-if="!edit">Dodaj Opiekuna - Uczelnia</h3>
        <h3 v-else>Edytuj Opiekuna - Uczelnia {{ id }}</h3>

        <el-form ref="USAddForm" :rules="rules" :model="model">
          <div>
<!--            <el-form-item label="Nazwa użytkownika:" prop="username">-->
<!--              <el-input v-model="model.username"></el-input>-->
<!--            </el-form-item>-->
            <el-form-item label="Imię:" prop="name">
              <el-input v-model="model.name"></el-input>
            </el-form-item>
            <el-form-item label="Nazwisko:" prop="surname">
              <el-input v-model="model.surname"></el-input>
            </el-form-item>
            <el-form-item label="Email:" prop="email">
              <el-input v-model="model.email"></el-input>
            </el-form-item>
            <el-form-item label="Wydział:" prop="department">
              <el-select
                v-model="model.departmentId"
                placeholder="Zacznij wpisywać nazwę wydziału"
                :no-match-text="'Brak wyników'"
                filterable
              >
                <el-option
                  v-for="item in departments"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
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
  name: "UniversitySupervisorsAdd",
  props: {
    edit: String,
    id: String,
  },
  data() {
    return {
      componentName: "USAddForm",
      editingId: -1,
      departments: [],
      model: {
        username: "",
        name: "",
        surname: "",
        email: "",
        departmentId: "",
      },
      rules: {
        name: [{ required: true, message: "Pole wymagane", trigger: "change" }],
        surname: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        email: [
          {
            required: true,
            type: "email",
            message: "Wpisz poprawny adres email",
            trigger: "change",
          },
        ],
      },
    };
  },
  methods: {
    handleSubmit() {
      if (!this.valid(this.componentName)) return false;

      var data = {
        name: this.model.name,
        surname: this.model.surname,
        email: this.model.email,
        departmentId: this.model.departmentId,
      };

      var url = this.api + "/university-supervisors/token";
      if (this.edit) {
        url = this.api + "/university-supervisors/" + this.editingId;
      }
      this.axios({
        method: "POST",
        url: url,
        data: data,
      })
        .then((resp) => {
          console.log(resp);
          this.$message({
            message: "Operacja wykonana poprawnie",
            type: "success",
          });
            this.$router.push("/university-supervisors/list");

        })
        .catch((error) => {
          console.log(error);
        });
    },
    loadDepartments() {
      this.axios({
        method: "GET",
        url: this.api + "/departments",
      }).then((resp) => {
        console.log(resp);
        this.departments = resp.data;
      });
    },
    loadEntity() {
      this.axios({
        method: "GET",
        url: this.api + "/university-supervisors/" + this.editingId,
      }).then((resp) => {
        console.log(resp);
        if (resp.data) {
          this.model.username = resp.data.username;
          this.model.name = resp.data.name;
          this.model.surname = resp.data.surname;
          this.model.email = resp.data.email;
          this.model.departmentId = resp.data.department.id;
        } else {
          this.$message({
            message: "Brak opiekuna o ID = " + this.editingId,
            type: "error",
          });
          this.$router.push("/university-supervisors");
        }
      });
    },
  },
  mounted() {
    this.loadDepartments();

    if (this.edit) {
      this.editingId = this.$route.params.id;
      this.loadEntity(this.editingId);
    }
  },
};
</script>

<style scoped></style>
