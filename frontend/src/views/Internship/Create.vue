<template>
  <el-row>
    <el-col :span="24">
      <el-form ref="InternshipCreateForm" :rules="rules" :model="model">
        <div class="studentGroups">
          <h3>Formularz umowy o przeprowadzenie praktyk</h3>
        </div>
        <h5>Twoje dane:</h5>
        <el-row :gutter="20">
          <el-col :span="6" :md="12" :lg="12" :xl="12" :xs="24" :sm="12">
            <el-form-item label="Imię:" prop="name">
              <el-input v-model="model.name" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" :md="12" :lg="12" :xl="12" :xs="24" :sm="12">
            <el-form-item label="Nazwisko:" prop="surname">
              <el-input v-model="model.surname" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="Numer albumu:" prop="number_index">
          <el-input v-model="model.number_index"></el-input>
        </el-form-item>
        <el-form-item label="Adres:" prop="address">
          <el-input v-model="model.address"></el-input>
        </el-form-item>
        <el-form-item label="Kierunek studiów:" prop="fieldOfStudy">
          <el-input v-model="model.fieldOfStudy"></el-input>
        </el-form-item>


        <h5>Dane firmy przyjmującej na praktykę</h5>

        <el-form-item label="Firma:" prop="companyId">
          <el-select
                  v-model="model.companyId"
                  placeholder="Zacznij wpisywać nazwę firmy"
                  @change="getCompanySupervisors()"
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
        <div v-if="model.companyId">
          <h5>Wybrana firma:</h5>
          TODO {{selectedCompany}}
        </div>
        <el-form-item
                label="Opiekun:"
                prop="companySupervisorId"
                v-if="model.companyId"
        >
          <el-select
                  v-model="model.companySupervisorId"
                  placeholder="Zacznij wpisywać imię i nazwisko opiekuna"
                  :no-match-text="'Brak wyników'"
                  filterable
          >
            <el-option
                    v-for="item in companySupervisors"
                    :key="item.id"
                    :value="item.id"
                    :label="item.name + ' ' + item.surname"
            >
            </el-option>
          </el-select>
        </el-form-item>


        <h5>Dane dodatkowe</h5>
        <el-form-item label="Czy praktyka jest odpłatna?">
          <el-switch
                  v-model="model.payment"
                  active-text="Płatna"
                  inactive-text="Bezpłatna"
          >
          </el-switch>
        </el-form-item>

        <el-form-item label="Stanowisko:" prop="position">
          <el-input v-model="model.position"></el-input>
        </el-form-item>
        <el-row>
          <el-button type="success" @click="handleSubmit"
          >Zapisz dane</el-button
          >
        </el-row>
      </el-form>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "CreateInternship",
  data() {
    return {
      componentName: "InternshipCreateForm",
      username: "",
      studentGroups: [],
      studentInternships: [],
      companies: [],
      companySupervisors: [],
      model: {
        companySupervisorId: "",
        companyId: '',
        name: '',
        surname: '',
        payment: false,
        position: '',
        address: '',
        fieldOfStudy: '',
        number_index: '',
      },
      rules: {
        companyId: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        companySupervisorId: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        payment: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        position: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        address: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        fieldOfStudy: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        number_index: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],

      },
    };
  },
  methods: {
    getData(nextFunction) {
      this.axios
        .get(this.api + "/logged/session", {
          headers: { "Content-Type": "application/json" },
        })
        .then((resp) => {
          this.username = resp.data.username;
          this.model.name = resp.data.name;
          this.model.surname = resp.data.surname;
          //get student groups
          this.axios
            .get(this.api + "/students/groups", {
              params: { username: this.username },
            })
            .then((resp) => {
              this.studentGroups = [resp.data];
              if (resp.status === 204) {
                this.$router.push("/student/your-practice");
              }
              nextFunction();
            })
            .catch(() => {
              this.$message({
                message: "Błąd komunikacji z serwerem",
                type: "Error",
              });
              this.$router.push("/");
            });

        })
        .catch(() => {
          this.$message({
            message: "Błąd komunikacji z serwerem",
            type: "Error",
          });
          this.$router.push("/");
        });
    },
    getCompanies() {
      this.axios({
        method: "GET",
        url: this.api + "/companies",
      }).then((resp) => {
        this.companies = resp.data;
      });
    },
    getCompanySupervisors() {
      this.axios({
        method: "GET",
        url: this.api + "/company-supervisors/company/" + this.model.companyId,
      }).then((resp) => {
        this.companySupervisors = resp.data;
      });
    },
    getStudentInternship(){
      this.axios
              .get(this.api + "/internship", {
                params: { username: this.username },
              })
              .then((resp) => {
                this.studentInternships = [resp.data];
                if (resp.status === 200) {
                  this.$router.push("/student/your-practice");
                }
              })
              .catch(() => {
                this.$message({
                  message: "Błąd komunikacji z serwerem",
                  type: "Error",
                });
                this.$router.push("/");
              });
    },
    handleSubmit() {
      this.$refs[this.componentName].validate((valid) => {
        if (valid) {
          this.axios({
            method: "POST",
            url: this.api + "/internship/create",
            data: {
              //studentUsername: this.username,
              companyId: this.model.companyId,
              companySupervisorId: this.model.companySupervisorId,
              payment: this.model.payment,
              position: this.model.position,
              address: this.model.address,
              fieldOfStudy: this.model.fieldOfStudy,
              number_index: this.model.number_index
            },
            headers: {
              "Content-Type": "application/json"
            }
          }).then(() => {
            this.$message({
              message: "Pomyślnie dodano dane.",
              type: "success",
            });
            this.$router.push("/student/your-practice");
          })
              .catch((err) => {
                console.log(err);
                this.$message({
                  message: "Błąd komunikacji z serwerem.",
                  type: "Error",
                });
              });
        }
      });
    },
  },
  mounted() {
    this.getData(()=>{
      this.getStudentInternship();
    });
    this.getCompanies();
  },
  computed: {
    selectedCompany(){
      return this.companies.filter(value => {
        console.log(value);
        return value.id == this.model.companyId;
      })[0]
    }
  }
};
</script>

<style scoped>

  h3, h4, h5{
    text-align: left;
  }

</style>
