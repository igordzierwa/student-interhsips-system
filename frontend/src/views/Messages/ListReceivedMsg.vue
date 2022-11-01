<template>
  <el-row>
    <el-col :span="24">
      <h4>Lista odebranych wiadomości</h4>
      <el-table
        :data="model.list"
        style="width: 100%"
        id="ReceivedMsgList"
        empty-text="Brak wyników"
      >
        <el-table-column
          prop="sender.name"
          label="Imię nadawcy"
        ></el-table-column>
        <el-table-column
          prop="sender.surname"
          label="Nazwisko nadawcy"
        ></el-table-column>
        <el-table-column prop="topic" label="Temat"></el-table-column>
        <el-table-column fixed="right" label="Akcje" width="240">
          <template #default="scope">
            <div class="flex-btn">
              <el-button type="primary" @click="handleShowMessage(scope.row.id)"
                >Podgląd</el-button
              >
              <el-button type="danger" @click="handleDelete(scope.row.id)"
                >Usuń</el-button
              >
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
  <el-dialog
    title="Potwierdzenie"
    v-model="model.dialogVisible"
    width="30%"
    :before-close="handleConfirmDelete"
  >
    <span>Czy na pewno chcesz usunąć wiadomość?</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="model.dialogVisible = false">Anuluj</el-button>
        <el-button type="primary" @click="handleConfirmDelete"
          >Potwierdzam</el-button
        >
      </span>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: "ReceivedMsgList",
  data() {
    return {
      componentName: "ReceivedMessagesList",
      model: {
        list: [],
        toDeleteId: -1,
        dialogVisible: false,
      },
      rules: {
        name: [{ required: true, message: "Pole wymagane", trigger: "change" }],
        surname: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    handleLoadMessages() {
      this.axios
        .get(this.api + "/logged/session", {
          headers: { "Content-Type": "application/json" },
        })
        .then((resp) => {
          this.axios({
            method: "GET",
            url: this.api + "/messages-received/" + resp.data.id,
          })
            .then((resp) => {
              console.log(resp);
              this.model.list = resp.data;
            })
            .catch((error) => {
              this.$message({
                message: "Błąd pobierania. Error: " + error.data.message,
                type: "error",
              });
            });
        });
    },
    handleDelete(variable) {
      console.log(variable);
      this.model.toDeleteId = variable;
      this.model.dialogVisible = true;
    },
    handleConfirmDelete() {
      this.model.dialogVisible = false;
      this.axios({
        method: "DELETE",
        url: this.api + "/messages-received/" + this.model.toDeleteId,
      })
        .then((resp) => {
          console.log(resp);
          this.message({
            message: "Poprawnie usunięto wiadomość.",
            type: "success",
          });
          this.handleLoadMessages();
        })
        .catch((error) => {
          console.log(error);
          this.message({
            message: "Błąd przy usuwaniu wiadomości.",
            type: "error",
          });
        });
    },
    handleShowMessage(id) {
      console.log(id);
      this.$router.push("/messages/" + id);
    },
  },
  mounted() {
    this.handleLoadMessages();
  },
};
</script>
