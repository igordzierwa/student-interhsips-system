<template>
  <el-row>
    <el-col :span="24">
      <h4>Lista firm</h4>
      <el-table
        :data="model.list"
        style="width: 100%"
        id="CompaniesList"
        empty-text="Brak wyników"
      >
        <el-table-column
          prop="companyName"
          label="Nazwa firmy"
        ></el-table-column>
        <el-table-column prop="nip" label="NIP"></el-table-column>
        <el-table-column prop="regon" label="REGON"></el-table-column>
        <el-table-column prop="address.city" label="Adres"></el-table-column>
        <el-table-column fixed="right" label="Akcje" width="330">
          <template #default="scope">
            <div class="flex-btn">
              <el-button
                type="primary"
                @click="handleShowSupervisors(scope.row.id)"
                >Opiekunowie</el-button
              >
              <el-button type="primary" @click="handleEdit(scope.row.id)"
                >Edytuj</el-button
              >
              <el-button type="danger" @click="handleDelete(scope.row.id)"
                         v-if="hasRole(['admin'])"
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
    <span>Czy na pewno chcesz usunąć firmę?</span>
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
  name: "CompaniesList",
  data() {
    return {
      componentName: "CompaniesList",
      model: {
        list: [],
        toDeleteId: -1,
        dialogVisible: false,
      },
      rules: {
        nip: [{ required: true, message: "Pole wymagane", trigger: "change" }],
        regon: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        companyName: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    handleLoadCompanies() {
      this.axios({
        method: "GET",
        url: this.api + "/companies",
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
      this.$router.push("/companies/" + id);
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
        url: this.api + "/companies/" + this.model.toDeleteId,
      })
        .then((resp) => {
          console.log(resp);
          this.message({
            message: "Poprawnie usunięto firmę.",
            type: "success",
          });
          this.handleLoadCompanies();
        })
        .catch((error) => {
          console.log(error);
          this.message({
            message: "Błąd przy usuwaniu firmy.",
            type: "error",
          });
        });
    },
    handleShowSupervisors(id) {
      this.$router.push("/company-supervisors/list/" + id);
    },
  },
  mounted() {
    this.handleLoadCompanies();
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
