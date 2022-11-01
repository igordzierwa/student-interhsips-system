<template>
  <el-row>
    <el-col :span="24">
      <h4>Lista studentów</h4>
      <el-table
        :data="model.list"
        style="width: 100%"
        id="studentsList"
        empty-text="Brak wyników"
      >
        <el-table-column
          prop="username"
          label="Nazwa użytkownika"
        ></el-table-column>
        <el-table-column prop="name" label="Imię"></el-table-column>
        <el-table-column prop="surname" label="Nazwisko"></el-table-column>
        <el-table-column prop="department.shortname" label="Wydział"></el-table-column>
        <el-table-column
          prop="universitySupervisor"
          label="Opiekun - Uczelnia"
        >
          <template #default="scope" >
            <span v-if="scope.row.universitySupervisor">
              {{scope.row.universitySupervisor.name}}
              {{scope.row.universitySupervisor.surname}}
            </span>
            <span v-else>
              Brak
            </span>
          </template>

        </el-table-column>
        <el-table-column
          prop="companySupervisor"
          label="Opiekun - Pracodawca"
        >
          <template #default="scope" >
            <span v-if="scope.row.companySupervisor">
              {{scope.row.companySupervisor.name}}
              {{scope.row.companySupervisor.surname}}
            </span>
            <span v-else>
              Brak
            </span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="Akcje" width="240" v-if="hasRole(['university_supervisor', 'admin'])">
          <template #default="scope">
            <div class="flex-btn">
              <el-button type="primary" @click="handleEdit(scope.row.id)"
                >Edytuj</el-button
              >
              <el-button type="danger" @click="handleDelete(scope.row.id)"
                >Usuń</el-button
              >
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
    <span>Czy na pewno chcesz usunąć studenta?</span>
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
  name: "StudentsList",
  data() {
    return {
      componentName: "StudentsList",
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
        department: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    handleLoadStudents() {
      this.axios({
        method: "GET",
        url: this.api + "/students",
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
    handleDelete(variable) {
      console.log(variable);
      this.model.toDeleteId = variable;
      this.model.dialogVisible = true;
    },
    handleConfirmDelete() {
      this.model.dialogVisible = false;
      this.axios({
        method: "DELETE",
        url: this.api + "/students/" + this.model.toDeleteId,
      })
        .then((resp) => {
          console.log(resp);
          this.message({
            message: "Poprawnie usunięto studenta.",
            type: "success",
          });
          this.handleLoadStudents();
        })
        .catch((error) => {
          console.log(error);
          this.message({
            message: "Błąd przy usuwaniu studenta.",
            type: "error",
          });
        });
    },
    handleEdit(id) {
      console.log(id);
      this.$router.push("/admin/students/" + id);
    },
  },
  mounted() {
    this.handleLoadStudents();
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
