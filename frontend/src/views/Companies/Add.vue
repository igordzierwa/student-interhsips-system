<template>
  <el-row>
    <el-col :span="24">
      <div class="grid-content bg-purple-dark">
        <h3 v-if="!edit">Dodaj firmę</h3>
        <h3 v-else>Edytuj firmę {{ id }}</h3>

        <el-form
          ref="CompanyAddForm"
          :rules="rules"
          :model="model"
          v-loading="loading"
        >
          <div>
            <el-form-item label="Nazwa firmy:" prop="companyName">
              <el-input v-model="model.companyName"></el-input>
            </el-form-item>
            <el-form-item label="NIP:" prop="nip">
              <el-input v-model="model.nip"></el-input>
            </el-form-item>
            <el-form-item label="REGON:" prop="regon">
              <el-input v-model="model.regon"></el-input>
            </el-form-item>
            <el-form-item label="Ulica:" prop="street">
              <el-input v-model="model.street"></el-input>
            </el-form-item>
            <el-form-item label="Kod pocztowy:" prop="zipCode">
              <el-input v-model="model.zipCode"></el-input>
            </el-form-item>
            <el-form-item label="Miasto:" prop="city">
              <el-input v-model="model.city"></el-input>
            </el-form-item>
            <el-row>
              <el-button type="success" @click="handleSubmit">Zapisz</el-button>
            </el-row>
          </div>
        </el-form>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "CompanyAdd",
  props: {
    edit: String,
    id: String,
  },
  data() {
    return {
      componentName: "CompanyAddForm",
      editingId: -1,
      model: {
        companyName: "",
        nip: "",
        regon: "",
        city: "",
        street: "",
        zipCode: "",
      },
      rules: {
        companyName: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        nip: [{ required: true, message: "Pole wymagane", trigger: "change" }],
        regon: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        street: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        city: [{ required: true, message: "Pole wymagane", trigger: "change" }],
        zipCode: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    handleSubmit() {
      if (!this.valid(this.componentName)) return false;

      var data = {
        companyName: this.model.companyName,
        nip: this.model.nip,
        regon: this.model.regon,
        address: {
          street: this.model.street,
          zipCode: this.model.zipCode,
          city: this.model.city,
        },
      };
      var url = this.api + "/companies";
      if (this.edit) {
        url = url + "/" + this.editingId;
      }
      this.axios({
        method: "POST",
        url: url,
        data: data,
      }).then((resp) => {
        console.log(resp);
        this.$message({
          message: "Operacja wykonana poprawnie",
          type: "success",
        });
        this.$router.push("/companies/list");
      });
    },
    loadEntity() {
      this.axios({
        method: "GET",
        url: this.api + "/companies/" + this.editingId,
      }).then((resp) => {
        console.log(resp);
        if (resp.data) {
          this.model.companyName = resp.data.companyName;
          this.model.nip = resp.data.nip;
          this.model.regon = resp.data.regon;
          this.model.street = resp.data.address.street;
          this.model.city = resp.data.address.city;
          this.model.zipCode = resp.data.address.zipCode;
        } else {
          this.$message({
            message: "Brak firmy o ID = " + this.editingId,
            type: "error",
          });
          this.$router.push("/companies/list");
        }
      });
    },
  },
  mounted() {
    if (this.edit) {
      this.editingId = this.$route.params.id;
      this.loadEntity(this.editingId);
    }
  },
};
</script>

<style scoped></style>
