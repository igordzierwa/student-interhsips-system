<template>
  <el-row>
    <el-col :span="24">
      <h4>Grupy studenckie</h4>
      <el-table
        :data="model.list"
        style="width: 100%"
        id="studentGroupsList"
        empty-text="Brak wyników"
      >
        <el-table-column
          prop="studentGroupName"
          label="Nazwa grupy studenckiej"
        ></el-table-column>
        <el-table-column
          prop="universitySupervisor.name"
          label="Imię opiekuna"
        ></el-table-column>
        <el-table-column
            prop="universitySupervisor.surname"
            label="Nazwisko opiekuna"
        ></el-table-column>
        <el-table-column
            prop="universitySupervisor.department.shortname"
            label="Wydział"
        ></el-table-column>
        <el-table-column fixed="right" label="Akcje" width="330" v-if="hasRole(['university_supervisor', 'admin'])">
          <template #default="scope">
            <div class="flex-btn">
              <el-button type="default" @click="handleStudentsAdding(scope.row.id)">
                Dodaj studentów
              </el-button>
              <el-button type="primary" @click="handleEdit(scope.row.id)">
                Edytuj
              </el-button>
              <el-button type="danger" @click="handleDelete(scope.row.id)">
                Usuń
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "DepartmentList",
  data() {
    return {
      componentName: "DepartmentAddForm",
      model: {
        list: [],
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
        url: this.api + "/studentGroups",
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
    handleStudentsAdding(id){
      this.$router.push(`/admin/student-groups/${id}/add-students`)
    },
    handleEdit(id) {
      console.log(id);
      this.$router.push("/student-groups/" + id);
    },
    handleDelete(id) {
      console.log(id);
      //todo delete
      this.$message({
        message: "TODO DELETING",
        type: "error",
      });
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
