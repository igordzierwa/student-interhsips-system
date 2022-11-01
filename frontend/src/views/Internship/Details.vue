<template>
    <div class="full">
        <el-row type="flex" class="row-bg" justify="space-around">
            <el-col :span="18">
                <h4>Szczegóły praktyki</h4>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="12">
                <el-result icon="success" title="Zatwierdzone przez Opiekuna z firmy"
                           subTitle="Gratulacje! twoja praktyka została zatwierdzona!"
                           v-if="model.companySupervisorAccepted">
                </el-result>
                <el-result icon="error" title="Niezatwierdzone przez Opiekuna z firmy"
                           v-else>
                  <template #extra>
                    <span v-if="!model.editionNotAllowed">
                      Uzupełnij wymagane dane i prześlij do oceny
                    </span>
                    <span v-else>
                      Twoja praktyka oczekuje na zatwierdzenie przez opiekuna
                    </span>
                  </template>
                </el-result>
            </el-col>
            <el-col :span="12">
                <el-result icon="success" title="Zatwierdzone przez Opiekuna z uczelni"
                           subTitle="Gratulacje! twoja praktyka została zatwierdzona!"
                           v-if="model.universitySupervisorAccepted">
                </el-result>
                <el-result icon="error" title="Niezatwierdzone przez Opiekuna z uczelni"
                           v-else>
                  <template #extra>
                    <span v-if="!model.editionNotAllowed">
                      Uzupełnij wymagane dane i prześlij do oceny
                    </span>
                    <span v-else>
                      Twoja praktyka oczekuje na zatwierdzenie przez opiekuna
                    </span>
                  </template>
                </el-result>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="12">
                <h5>Ocena opiekuna z firmy</h5>
                <el-progress type="circle" :percentage="100" status="success">
                <span v-if="model.gradesFromCompanySupervisor">
                  {{ model.gradesFromCompanySupervisor }}
                </span>
                    <span v-else>
                Brak
              </span>
                </el-progress>
                <span v-if="model.additionalInformation">
            <h5>Dodatkowe informacje</h5>
            {{ model.additionalInformation }}
          </span>
            </el-col>
            <el-col :span="12">
                <h5>Ocena opiekuna z uczelni</h5>
                <el-progress type="circle" :percentage="100" status="success">
                <span v-if="model.gradeFromUniversitySupervisor">
                  {{ model.gradeFromUniversitySupervisor }}
                </span>
                    <span v-else>
                Brak
              </span>
                </el-progress>
            </el-col>
        </el-row>

        <el-row type="flex" class="row-bg" justify="space-around">
            <el-col :span="18">
                <el-form
                        ref="InternshipDetails"
                        :model="model"
                        :rules="rules"
                >
                    <el-form-item label="Data rozpoczęcia praktyki" prop="dateStarted">
                        <el-input
                                type="date"
                                v-model="model.dateStarted"
                                :disabled="model.editionNotAllowed"
                        >
                        </el-input>
                    </el-form-item>
                    <el-form-item label="Data zakończenia praktyki" prop="dateFinished">
                        <el-input
                                type="date"
                                v-model="model.dateFinished"
                                :disabled="model.editionNotAllowed"
                        >
                        </el-input>
                    </el-form-item>
                    <el-form-item label="Ilość godzin" prop="hoursCount">
                        <el-input
                                type="number"
                                v-model="model.hoursCount"
                                :disabled="model.editionNotAllowed"
                        >
                        </el-input>
                    </el-form-item>
                    <el-form-item label="Sprawozdanie z praktyki" prop="report">
                        <el-input
                                type="textarea"

                                :rows="4"
                                maxlength="500"
                                show-word-limit
                                v-model="model.report"
                                :disabled="model.editionNotAllowed"
                        >
                        </el-input>
                    </el-form-item>


                    <el-row>
                        <div class="buttons">
                            <el-button @click="handleSubmit" v-if="!model.editionNotAllowed">Zapisz i wyślij do akceptacji</el-button>
                            <el-button @click="handleResend" v-if="model.editionNotAllowed && (!model.companySupervisorAccepted || !model.universitySupervisorAccepted)">Wyślij ponownie prośbę o akceptację</el-button>
                            <el-button @click="handleGenerateCert" v-if="model.companySupervisorAccepted && model.universitySupervisorAccepted">Wygeneruj certyfikat ukończenia praktyk</el-button>


                          <!--              <el-button @click="handleSendEmailToCompanySupervisor" v-if="!model.companySupervisorAccepted && !model.editionNotAllowed"-->
                            <!--              >Poproś opiekuna firmy o ocenę</el-button>-->
                            <!--              <el-button @click="handleSendEmailToUniversitySupervisor" v-if="model.companySupervisorAccepted"-->
                            <!--              >Poproś opiekuna uczelni o ocenę</el-button>-->
                        </div>
                    </el-row>
                </el-form>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "Details",
        data() {
            return {
              componentName: 'InternshipDetails',
                model: {
                    universitySupervisorAccepted: "",
                    companySupervisorAccepted: "",
                    dateStarted: "",
                    dateFinished: "",
                    hoursCount: "",
                    report: "",
                    gradesFromCompanySupervisor: "",
                    additionalInformation: "",
                    gradeFromUniversitySupervisor: "",
                    editionNotAllowed: ""
                },
                rules: {
                    dateStarted: [
                        {required: true, message: "Pole wymagane", trigger: "change"},
                    ],
                    dateFinished: [
                        {required: true, message: "Pole wymagane", trigger: "change"},
                    ],
                    hoursCount: [
                        {required: true, message: "Pole wymagane", trigger: "change"},
                    ],
                    report: [
                        {required: true, message: "Pole wymagane", trigger: "change"},
                    ]

                },
            }
        },
        methods: {

          handleGenerateCert(){
            //Create a Blob from the PDF Stream

              this.axios({
                method: "POST",
                url: this.api + `/certificate`,
                responseType: 'blob'
              }).then((response) => {
                const file = new Blob(
                        [response.data],
                        {type: 'application/pdf'});
                //Build a URL from the file
                const fileURL = URL.createObjectURL(file);
                //Open the URL on new Window
                window.open(fileURL);
              });

          },
          handleResend(){
            this.axios({
              method: "POST",
              url: this.api + `/internship/resend-email`
            }).then(() => {
              this.model.editionNotAllowed = true;
              this.$message({
                message: "Poprawnie wysłano powiadomienie.",
                type: "success",
              });
            });
          },
            handleSubmit() {
                this.$refs[this.componentName].validate((valid) => {
                    if (valid) {
                      this.axios({
                        method: "POST",
                        url: this.api + `/internship/${this.$route.params.id}/edit-student-fields`,
                        data: {
                          dateStarted: this.model.dateStarted,
                          dateFinished: this.model.dateFinished,
                          hoursCount: this.model.hoursCount,
                          report: this.model.report
                        }
                      }).then(() => {
                        this.model.editionNotAllowed = true;
                        this.$message({
                          message: "Poprawnie zapisano dane.",
                          type: "success",
                        });
                      });
                    }
                })
            },
            getInternshipData() {
                let internshipId = this.$route.params.id;
                this.axios({
                    method: "GET",
                    url: this.api + "/internship/" + internshipId,
                }).then((resp) => {
                    this.model = resp.data;

                    console.log(this.model);
                });
            }
        },
        mounted() {
            this.getInternshipData();
        }
    }
</script>

<style scoped>
</style>