<template>
  <el-row class="full">
    <el-col :span="24">
      <div class="studentGroups">
        <h2>Twoja grupa studencka</h2>
        <el-card class="box-card" v-if="showGroups">
          <ul>
            <li>Nazwa grupy: {{studentGroups.studentGroupName}}</li>
            <li>Opiekun - uczelnia: {{studentGroups.universitySupervisor.name}} {{studentGroups.universitySupervisor.surname}}</li>
            <li>Wydział: {{studentGroups.universitySupervisor.department.shortname}}</li>
          </ul>
        </el-card>
        <div v-else>
          <p>
            Nie posiadasz żadnych grup, poproś opiekuna o dołączenie do grupy
            aby móc rozpocząć praktykę!
          </p>
        </div>
      </div>
      <br />
      <br />
      <div class="internship">
        <h2>Twoja praktyka</h2>

        <el-card class="box-card" v-if="showInternships">
          <ul>
            <li>Firma: {{studentInternships.companySupervisor.company.companyName}}</li>
            <li>Opiekun - firma: {{studentInternships.companySupervisor.name}} {{studentInternships.companySupervisor.surname}}</li>
            <li>Czy praktyka jest zakończona?:
              <span v-if="studentInternships.universitySupervisorAccepted&&studentInternships.companySupervisorAccepted">
                Tak
              </span>
              <span v-else>
                Nie
              </span>
            </li>
          </ul>
          <el-button type="primary" @click="handleEdit">
            Szczegóły
          </el-button>
        </el-card>
        <div v-else>
          <p>Nie posiadasz aktywnej praktyki, dodaj dane aby rozpocząć!</p>
          <el-button type="success" @click="handleRoute">
            Dodaj dane
          </el-button>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "Internship",
  data() {
    return {
      username: '',
      showGroups: false,
      studentGroups: {
        universitySupervisor:{
          department: {

          }
        }
      },
      showInternships: false,
      studentInternships: {
        companySupervisor: {
          company:{

          }
        }
      }
    }
  },
  methods:{
    getData(nextFunction) {
      this.axios
          .get(this.api + "/logged/session", {
            headers: { "Content-Type": "application/json" },
          })
          .then((resp) => {
            this.username = resp.data.username;
            console.log(resp.data);
            this.axios
                .get(this.api + "/students/groups",
                    {params: {username: this.username}})
                .then((resp) => {
                  this.studentGroups = resp.data;
                  this.showGroups = resp.status === 200;
                 nextFunction();
                })
                .catch((error) => {
                  console.log(error)
                  this.showGroups = false;
                  this.$message({
                    message: "Błąd komunikacji z serwerem",
                    type: "Error",
                  });
                });
          })
          .catch((error) => {
            console.log(error);
            this.$message({
              message: "Błąd komunikacji z serwerem",
              type: "Error",
            });
          });
    },
    loadInternshipInfo(){
      console.log(this.username)
      this.axios
              .get(this.api + "/internship",
                      {params: {username: this.username}})
              .then((resp) => {
                this.studentInternships =resp.data;
                this.showInternships = resp.status === 200;

              })
              .catch((error) => {
                console.log(error)
                this.showInternships = false;
                this.$message({
                  message: "Błąd komunikacji z serwerem",
                  type: "Error",
                });
              });
    },
    handleRoute() {
      this.$router.push("/student/your-practice/create");
    },
    handleEdit(){
      this.$router.push('/student/your-practice/details/' + this.studentInternships.id);
    }
  },
  mounted() {
    this.getData(()=>{
      this.loadInternshipInfo();
    });
  },
};
</script>

<style scoped>
.full{
  text-align: left;
}

</style>
