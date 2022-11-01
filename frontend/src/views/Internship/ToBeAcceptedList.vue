<template>
  <el-row>
    <el-col :span="24">
      <h4>Lista praktykantów do oceny</h4>
      <el-table
          :data="model.list"
          style="width: 100%"
          id="studentsList"
          empty-text="Brak wyników"
      >
        <el-table-column prop="internship.student.username" label="Nazwa użytkownika"></el-table-column>
        <el-table-column prop="internship.student.name" label="Imię"></el-table-column>
        <el-table-column prop="internship.student.surname" label="Nazwisko"></el-table-column>
        <el-table-column prop="internship.student.department.shortname" label="Wydział"></el-table-column>
        <el-table-column label="Opiekun - Uczelnia">
          <template #default="scope" >
            <span v-if="scope.row.internship.student.universitySupervisor">
              {{scope.row.internship.student.universitySupervisor.name}}
              {{scope.row.internship.student.universitySupervisor.surname}}
            </span>
          </template>

        </el-table-column>
        <el-table-column label="Firma">
          <template #default="scope" >
            <span v-if="scope.row.internship.companySupervisor">
              {{scope.row.internship.companySupervisor.company.companyName}}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="Opiekun - Pracodawca">
          <template #default="scope" >
            <span v-if="scope.row.internship.companySupervisor">
              {{scope.row.internship.companySupervisor.name}}
              {{scope.row.internship.companySupervisor.surname}}
            </span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="Akcje" width="240">
          <template #default="scope">
            <div class="flex-btn">
              <el-button type="primary" @click="routeToAccept(scope.row.token)">Przejdź do oceniania</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "ToBeAcceptedList",
  data() {
    return {
      username: "",
      userRoles: [],
      model: {
        list: []
      }
    }
  },
  methods: {
    getData(){
      this.axios
          .get(this.api + "/logged/session", {
            headers: { "Content-Type": "application/json" },
          })
          .then((resp) => {
            this.username = resp.data.username;
            this.userRoles = resp.data.roles;
            this.axios({
              method: "GET",
              url: this.api + "/internship/get-verification-tokens",
              params: {
                username: this.username
              }
            })
                .then((resp) => {
                  this.model.list = resp.data;
                })
          })
    },
    routeToAccept(validationToken){
      if(this.userRoles.includes('university_supervisor')){
        this.$router.push(`/university-supervisors/accept-internship?token=${validationToken}`);
      } else {
        this.$router.push(`/company-supervisors/accept-internship?token=${validationToken}`);
      }
    }
  },
  mounted(){
    this.getData();
  }
}
</script>

<style scoped>

</style>