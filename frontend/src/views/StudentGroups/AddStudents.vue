<template>
  <el-row>
    <el-table
        ref="multipleTable"
        :data="model.list"
        style="width: 100%"
        @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column
          prop="username"
          label="Nazwa użytkownika"
      ></el-table-column>
      <el-table-column prop="name" label="Imię"></el-table-column>
      <el-table-column prop="surname" label="Nazwisko"></el-table-column>
      <el-table-column prop="department" label="Wydział"></el-table-column>
      <el-table-column
          prop="universitySupervisor"
          label="Opiekun - Uczelnia"
      ></el-table-column>
      <el-table-column
          prop="companySupervisor"
          label="Opiekun - Pracodawca"
      ></el-table-column>
    </el-table>
    <div class="flex-btn">
      <el-button type="default" @click="handleStudentsAdding()">
        Dodaj studentów
      </el-button>
    </div>
  </el-row>
</template>

<script>
export default {
  name: "AddStudents",
  data() {
    return {
      multipleSelection: [],
      studentGroupId: '',
      model: {
        list: []
      }
    }
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    getStudentsWithoutGroupAssigned() {
      this.axios({
        method: "GET",
        url: this.api + "/studentsWithoutGroupAssigned"
      })
      .then((resp) => {
        this.model.list = resp.data;
      })
      .catch(() => {
        this.$message({
          message: "Błąd komunikacji z serwerem.",
          type: "error",
        });
      });
    },
    handleStudentsAdding(){
      let studentIds = []
      for (const selection of this.multipleSelection){
        studentIds.push(selection.id);
      }
      if (studentIds.length === 0){
        this.$message({
          message: "Nie wybrałeś żadnego studenta.",
          type: "error",
        });
        return;
      }
      this.axios({
        method: "POST",
        url: this.api + "/studentGroups/addStudent",
        data: {
          studentGroupId: this.studentGroupId,
          studentIds: studentIds
        },
        headers: {
          "Content-Type": "application/json",
        }
      })
          .then(() => {
            this.$message({
              message: "Poprawnie dodano studentów do grupy.",
              type: "success",
            });
          })
          .catch(() => {
            this.$message({
              message: "Błąd komunikacji z serwerem.",
              type: "error",
            });
          });
      this.getStudentsWithoutGroupAssigned();
    }
  },
  mounted(){
    this.studentGroupId = this.$route.params.id;
    this.getStudentsWithoutGroupAssigned();
  }
}
</script>

<style scoped>
</style>