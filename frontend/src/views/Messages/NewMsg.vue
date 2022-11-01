<template>
  <el-row>
    <el-col :span="24">
      <div class="grid-content bg-purple-dark">
        <h3>Wyślij nową wiadomość</h3>
        <el-form ref="MsgAddForm" :rules="rules" :model="model">
          <div>
            <el-form-item label="Tytuł:" prop="topic">
              <el-input v-model="this.model.topic"></el-input>
            </el-form-item>
            <el-form-item label="Treść:" prop="message">
              <el-input v-model="this.model.message" type="textarea"></el-input>
            </el-form-item>
            <el-form-item label="Adresat:" prop="recipient">
              <el-select
                v-model="this.model.recipientID"
                placeholder="Zacznij wpisywać adresata"
                :no-match-text="'Brak wyników'"
                filterable
              >
                <el-option
                  v-for="item in users"
                  :key="item.id"
                  :label="
                    item.username + ' - ' + item.name + ' ' + item.surname
                  "
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-row>
              <el-button type="success" @click="handleSubmit">Wyślij</el-button>
            </el-row>
          </div>
        </el-form>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "MessageAddForm",
  data() {
    return {
      componentName: "MsgAddForm",
      editingId: -1,
      users: [],
      model: {
        topic: "",
        message: "",
        recipientID: "",
      },
      rules: {
        topic: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        message: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        recipientID: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    handleSubmit() {
      if (!this.valid(this.componentName)) return false;

      this.axios
        .get(this.api + "/logged/session", {
          headers: { "Content-Type": "application/json" },
        })
        .then((resp) => {
          var data = {
            topic: this.model.topic,
            message: this.model.message,
            recipientID: this.model.recipientID,
            senderID: resp.data.id,
          };

          this.axios({
            method: "POST",
            url: this.api + "/messages-sent",
            data: data,
          })
            .then((resp) => {
              console.log(resp);
              this.$message({
                message: "Operacja wykonana poprawnie",
                type: "success",
              });
              this.$router.push("/messages/sent");
            })
            .catch((error) => {
              console.log(error);
            });
        });
    },
    loadUsers() {
      this.axios({
        method: "GET",
        url: this.api + "/users",
      })
        .then((resp) => {
          console.log(resp);
          this.users = resp.data;
        })
        .catch((error) => {
          this.$message({
            message: "Błąd pobierania. Error: " + error.data.message,
            type: "error",
          });
        });
    },
  },
  mounted() {
    this.loadUsers();
  },
};
</script>

<style scoped></style>
