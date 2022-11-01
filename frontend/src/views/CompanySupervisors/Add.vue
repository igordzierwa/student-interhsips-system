<template>
  <el-row>
    <el-col :span="24">
      <div class="grid-content bg-purple-dark">
        <h3 v-if="!edit">Dodaj Opiekuna - Firma</h3>
        <h3 v-else>Edytuj Opiekuna - Firma {{ id }}</h3>

        <el-form ref="CSAddForm" :rules="rules" :model="model">
          <div>
            <el-form-item label="Imię:" prop="name">
              <el-input v-model="model.name"></el-input>
            </el-form-item>
            <el-form-item label="Nazwisko:" prop="surname">
              <el-input v-model="model.surname"></el-input>
            </el-form-item>
            <el-form-item label="Email:" prop="email">
              <el-input v-model="model.email"></el-input>
            </el-form-item>
            <el-form-item label="Firma:" prop="company">
              <el-select
                v-loading="categorySelectLoading"
                v-model="model.companyId"
                placeholder="Zacznij wpisywać nazwę firmy"
                :no-match-text="'Brak wyników'"
                filterable
              >
                <el-option
                  v-for="item in companies"
                  :key="item.id"
                  :label="item.companyName"
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
  name: "CompanySupervisorsAdd",
  props: {
    edit: String,
    id: String,
  },
  data() {
    return {
      componentName: "CSAddForm",
      editingId: -1,
      companies: [],
      model: {
        name: "",
        surname: "",
        email: "",
        company: "",
      },
      rules: {
        // username: [
        //   { required: true, message: "Pole wymagane", trigger: "change" },
        // ],
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
        username: this.model.username,
        name: this.model.name,
        surname: this.model.surname,
        email: this.model.email,
        companyId: this.model.companyId,
      };

      var url = this.api + "/company-supervisors/token";
      if (this.edit) {
        url = this.api + "/company-supervisors/" + this.editingId;
      }
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
        this.$router.push("/company-supervisors/list");
      });
    },
    loadCompanies() {
      this.axios({
        method: "GET",
        url: this.api + "/companies",
      }).then((resp) => {
        console.log(resp);
        this.companies = resp.data;
      });
    },
    loadEntity() {
      this.axios({
        method: "GET",
        url: this.api + "/company-supervisors/" + this.editingId,
      }).then((resp) => {
        console.log(resp);
        if (resp.data) {
          this.model.username = resp.data.username;
          this.model.name = resp.data.name;
          this.model.surname = resp.data.surname;
          this.model.email = resp.data.email;
          this.model.companyId = resp.data.company.id;
        } else {
          this.$message({
            message: "Brak opiekuna o ID = " + this.editingId,
            type: "error",
          });
          this.$router.push("/company-supervisors");
        }
      });
    },
  },
  mounted() {
    this.loadCompanies();

    if (this.edit) {
      this.editingId = this.$route.params.id;
      this.loadEntity(this.editingId);
    }
  },
};
</script>

<style scoped></style>
