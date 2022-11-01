<template>
  <el-row>
    <el-col :span="24">
      <div class="grid-content bg-purple-dark">
        <h3 v-if="!edit"></h3>
        <h3 v-else>Edytuj wydział {{ id }}</h3>

        <el-form
          ref="SpecificMsgView"
          :rules="rules"
          :model="model"
          v-loading="loading"
        >
          <div>
            <el-form-item label="Nadawca:" prop="sender">
              <el-input v-model="model.senderName" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="Odbiorca:" prop="recipient">
              <el-input
                v-model="model.recipientName"
                :disabled="true"
              ></el-input>
            </el-form-item>
            <el-form-item label="Tytuł:" prop="topic">
              <el-input v-model="model.topic" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="Treść:" prop="message">
              <el-input v-model="model.message" :disabled="true"></el-input>
            </el-form-item>
          </div>
        </el-form>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "SpecificMessageView",
  data() {
    return {
      componentName: "SpecificMsgView",
      messageId: -1,
      model: {
        topic: "",
        message: "",
        senderName: "",
        recipientName: "",
        dialogVisible: false,
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
      });
    },
    loadEntity() {
      this.axios({
        method: "GET",
        url: this.api + "/messages/" + this.messageId,
      }).then((resp) => {
        console.log(resp);
        if (resp.data) {
          this.model.topic = resp.data.topic;
          this.model.message = resp.data.message;
          this.model.senderName =
            resp.data.sender.name + " " + resp.data.sender.surname;
          this.model.recipientName =
            resp.data.recipient.name + " " + resp.data.recipient.surname;
        } else {
          this.$message({
            message: "Brak wiadomości o ID = " + this.editingId,
            type: "error",
          });
          this.$router.push("/messages/received");
        }
      });
    },
  },
  mounted() {
    this.messageId = this.$route.params.id;
    this.loadEntity();
  },
};
</script>

<style scoped></style>
