<template>
  <el-row>
    <el-col :span="24">
      <h4>Lista wydziałów</h4>
      <el-table
        :data="model.list"
        style="width: 100%"
        id="departmentList"
        empty-text="Brak wyników"
      >
        <el-table-column prop="name" label="Nazwa"></el-table-column>
        <el-table-column prop="shortname" label="Skrót"></el-table-column>
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
    <span>Czy na pewno chcesz usunąć wydział?</span>
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
  name: "DepartmentList",
  data() {
    return {
      componentName: "DeparmentAddForm",
      model: {
        list: [],
        toDeleteId: -1,
        dialogVisible: false,
      },
      rules: {
        name: [{ required: true, message: "Pole wymagane", trigger: "change" }],
        short_name: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    handleLoadDepartments() {
      this.axios({
        method: "GET",
        url: this.api + "/departments",
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
      this.$router.push("/departments/" + id);
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
        url: this.api + "/departments/" + this.model.toDeleteId,
      })
        .then((resp) => {
          console.log(resp);
          this.message({
            message: "Poprawnie usunięto wydział.",
            type: "success",
          });
          this.handleLoadDepartments();
        })
        .catch((error) => {
          console.log(error);
          this.message({
            message: "Błąd przy usuwaniu wydziału.",
            type: "error",
          });
        });
    },
  },
  mounted() {
    this.handleLoadDepartments();
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
