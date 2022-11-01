<template>
  <el-row>
    <el-col :span="24">
      <h4>
        Lista Opiekunów
        <span v-if="companyName">w firmie {{ companyName }}</span>
      </h4>
      <el-table
        :data="model.list"
        style="width: 100%"
        id="departmentList"
        empty-text="Brak wyników"
      >
        <el-table-column
          prop="username"
          label="Nazwa użytkownika"
        ></el-table-column>
        <el-table-column prop="email" label="E-mail"></el-table-column>
        <el-table-column prop="name" label="Imię"></el-table-column>
        <el-table-column prop="surname" label="Nazwisko"></el-table-column>
        <el-table-column
          prop="company.companyName"
          label="Firma"
        ></el-table-column>
        <el-table-column fixed="right" label="Akcje" width="280" v-if="hasRole(['university_supervisor', 'student', 'admin'])">
          <template #default="scope">
            <div class="flex-btn">
              <el-button
                type="primary"
                @click="handleResendToken(scope.row.id)"
                v-if="!scope.row.active"
                >Ponowny email aktywacyjny</el-button
              >
              <div v-else>
                <el-button type="primary" @click="handleEdit(scope.row.id)"
                  >Edytuj</el-button
                >
                <el-button type="danger" @click="handleDelete(scope.row.id)"
                           v-if="hasRole(['admin'])"
                  >Usuń</el-button
                >
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
  <el-dialog
    title="Potwierdzenie"
    v-model="model.dialogVisible"
    width="30%"
    :before-close="handleConfirmDelete"
  >
    <span>Czy na pewno chcesz usunąć opiekuna?</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="model.dialogVisible = false">Anuluj</el-button>
        <el-button type="primary" @click="handleConfirmDelete"
          >Potwierdzam</el-button
        >
      </span>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: "CSList",
  data() {
    return {
      componentName: "CompanySupervisorsList",
      companyId: false,
      companyName: false,
      model: {
        list: [],
        toDeleteId: -1,
        dialogVisible: false,
      },
      rules: {
        name: [{ required: true, message: "Pole wymagane", trigger: "change" }],
        surname: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    handleLoadSupervisors(companyId) {
      var url = this.api + "/company-supervisors";
      if (companyId) url += "/company/" + companyId;

      this.axios({
        method: "GET",
        url: url,
      })
        .then((resp) => {
          console.log(resp);
          this.model.list = resp.data;

          if (companyId) {
            if (resp.data.length > 0) {
              this.companyName = resp.data[0].company.companyName;
            }
          }
        })
        .catch((error) => {
          this.$message({
            message: "Błąd pobierania. Error: " + error.data.message,
            type: "error",
          });
        });
    },
    handleEdit(id) {
      console.log(id);
      this.$router.push("/company-supervisors/" + id);
    },
    handleDelete(variable) {
      console.log(variable);
      this.model.toDeleteId = variable;
      this.model.dialogVisible = true;
    },
    handleConfirmDelete() {
      this.model.dialogVisible = false;
      this.axios({
        method: "DELETE",
        url: this.api + "/company-supervisors/" + this.model.toDeleteId,
      })
        .then((resp) => {
          console.log(resp);
          this.message({
            message: "Poprawnie usunięto opiekuna.",
            type: "success",
          });
          this.handleLoadSupervisors();
        })
        .catch((error) => {
          console.log(error);
          this.message({
            message: "Błąd przy usuwaniu opiekuna.",
            type: "error",
          });
        });
    },
    handleResendToken(id) {
      console.log(id);
      this.axios({
        method: "POST",
        url: this.api + "/company-supervisors/token/resend/" + id,
      })
        .then((resp) => {
          console.log(resp);
          this.$message({
            message: "Operacja wykonana poprawnie",
            type: "success",
          });
        })
        .catch((e) => {
          console.log(e);
          this.$message({
            message: "Operacja nie została wykonana poprawnie",
            type: "error",
          });
        });
    },
  },
  mounted() {
    if (this.$route.params.companyid) {
      this.companyId = this.$route.params.companyid;
    }
    this.handleLoadSupervisors(this.companyId);
  },
  computed: {
    userRole() {
      return this.$store.state.userData.roles;
    }
  }
};
</script>

<style scoped>
.flex-btn {
  display: flex;
}
</style>
