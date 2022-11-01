<template>
    <el-row>
        <el-col :span="24">
            <div class="studentGroups">
                <h3>Formularz umowy o przeprowadzenie praktyk</h3>
            </div>
            <h5>Twoje dane:</h5>
            <el-form>
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

            <el-form-item label="Wybierz firmę" prop="company">
                <el-select
                        v-model="model.companyId"
                        placeholder="Zacznij wpisywać nazwę firmy"
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

            <h5>Dane dodatkowe</h5>
            <el-form-item label="Czy praktyka jest odpłatna?">
                <el-switch
                        v-model="model.payment"
                        active-text="Płatna"
                        inactive-text="Bezpłatna"
                >
                </el-switch>
            </el-form-item>

            <el-form-item label="Stanowisko:" prop="fieldOfStudy">
                <el-input v-model="model.position"></el-input>
            </el-form-item>
                <el-row>
                    <el-button type="success" @click="handleSubmit">Zapisz</el-button>
                </el-row>
            </el-form>
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
                studentGroups: [],
                showInternships: false,
                studentInternships: [],
                companies: [],
                model:{
                    name: '',
                    surname: '',
                    companyId: '',
                    payment: false,
                    position: '',
                    address: '',
                    fieldOfStudy: '',
                    number_index: '',

                }

            }
        },
        methods:{
            getStudentGroups: function(functionEnded) {
                this.axios
                    .get(this.api + "/logged/session", {
                        headers: { "Content-Type": "application/json" },
                    })
                    .then((resp) => {
                        this.username = resp.data.username;
                        this.model.name = resp.data.name;
                        this.model.surname = resp.data.surname;
                        functionEnded();
                    })
                    .catch((error) => {
                        console.log(error);
                        this.loading = false;
                        this.$message({
                            message: "Błąd komunikacji z serwerem",
                            type: "Error",
                        });
                    });
            },
            handleStartInternship(){

            },

            handleSubmit(){
                console.log(this.model);
                this.axios({
                    method: "POST",
                    url: this.api + "/internship/step1",
                    data: this.model,
                }).then((resp) => {
                    console.log("addedd successfully", resp);
                    //this.companies = resp.data;
                }).catch((err) => {
                    console.log(err);
                })
            },
            loadCompanies() {
                this.axios({
                    method: "GET",
                    url: this.api + "/companies",
                }).then((resp) => {
                    console.log(resp);
                    this.companies = resp.data;
                });
            },
        },
        mounted(){
            this.getStudentGroups(() => {
                this.getStudentInternships();
            });

            this.loadCompanies();
        },
        computed: {
            selectedCompany(){
                return this.companies.filter(value => {
                    console.log(value);
                    return value.id == this.model.companyId;
                })[0]
            }
        }
    }
</script>

<style scoped>
</style>