<template>
  <el-row>
    <el-col :span="24">
      <h4>Lista Opiekunów</h4>
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
          prop="department.name"
          label="Wydział"
        ></el-table-column>
        <el-table-column fixed="right" label="Akcje" width="280">
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
  name: "USList",
  data() {
    return {
      componentName: "UniversitySupervisorsList",
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
    handleLoadSupervisors() {
      this.axios({
        method: "GET",
        url: this.api + "/university-supervisors",
      })
        .then((resp) => {
          console.log(resp);
          this.model.list = resp.data;
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
      this.$router.push("/university-supervisors/" + id);
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
        url: this.api + "/university-supervisors/" + this.model.toDeleteId,
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
        url: this.api + "/university-supervisors/token/resend/" + id,
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
    this.handleLoadSupervisors();
  },
};
</script>

<style scoped>
.flex-btn {
  display: flex;
}
</style>
