<template>
  <el-row>
    <el-col :span="24">
      <div class="grid-content bg-purple-dark">
        <h3 v-if="!edit">Dodaj wydział</h3>
        <h3 v-else>Edytuj wydział {{ id }}</h3>

        <el-form
          ref="DeparmentAddForm"
          :rules="rules"
          :model="model"
          v-loading="loading"
        >
          <div>
            <el-form-item label="Nazwa wydziału:" prop="name">
              <el-input v-model="model.name"></el-input>
            </el-form-item>
            <el-form-item label="Skrót:" prop="short_name">
              <el-input v-model="model.short_name"></el-input>
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
  name: "DepartmentAdd",
  props: {
    edit: String,
    id: String,
  },
  data() {
    return {
      componentName: "DeparmentAddForm",
      editingId: -1,
      model: {
        name: "",
        short_name: "",
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
    handleSubmit() {
      if (!this.valid(this.componentName)) return false;

      var data = {
        name: this.model.name,
        shortname: this.model.short_name,
      };

      var url = this.api + "/departments";
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
        this.$router.push("/departments/list");
      });
    },
    loadEntity() {
      this.axios({
        method: "GET",
        url: this.api + "/departments/" + this.editingId,
      }).then((resp) => {
        console.log(resp);
        if (resp.data) {
          this.model.name = resp.data.name;
          this.model.short_name = resp.data.shortname;
        } else {
          this.$message({
            message: "Brak wydziału o ID = " + this.editingId,
            type: "error",
          });
          this.$router.push("/departments/list");
        }
      });
    },
  },
  mounted() {
    if (this.edit) {
      // property przekazane w VueRouter
      this.editingId = this.$route.params.id;
      this.loadEntity(this.editingId);
    }
  },
};
</script>

<style scoped></style>
