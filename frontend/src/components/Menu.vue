<template>
  <div>
    <el-menu class="el-menu-demo" @select="handleSelect">
      <!--            <el-menu-item index="admin/import" v-show="userRole==='admin'">Import danych</el-menu-item>-->
      <el-menu-item index="" v-if="!hasRole(['any'])">
        <i class="el-icon-user-solid"></i>Zaloguj się
      </el-menu-item>
      <el-menu-item index="register" v-if="!hasRole(['any'])">
        <i class="el-icon-edit"></i>Zarejestruj się
      </el-menu-item>
      <el-menu-item index="your-account" v-if="hasRole(['any'])">
        <i class="el-icon-user-solid"></i>
        Twoje konto
      </el-menu-item>
      <el-menu-item index="student/your-practice" v-if="hasRole(['student'])">
        <i class="el-icon-notebook-1"></i>
        Twoja praktyka
      </el-menu-item>
      <el-menu-item index="internships-to-accept" v-else-if="hasRole(['university_supervisor', 'company_supervisor'])">
        <i class="el-icon-notebook-1"></i>
        Praktyki do oceny
      </el-menu-item>
      <el-submenu index="messages" v-if="hasRole(['any'])">
        <template #title>
          <i class="el-icon-message"></i>
          Wiadomości
        </template>
        <el-menu-item index="messages/new">Nowa wiadomość</el-menu-item>
        <el-menu-item index="messages/received">Odebrane</el-menu-item>
        <el-menu-item index="messages/sent">Wysłane</el-menu-item>
      </el-submenu>
      <el-submenu index="departments" v-if="hasRole(['any'])">
        <template #title>
          <i class="el-icon-school"></i>
          Wydziały
        </template>
        <el-menu-item index="departments/list">Lista wydziałów</el-menu-item>
        <el-menu-item index="departments/add" v-if="hasRole(['admin'])"
          >Dodaj wydział</el-menu-item
        >
      </el-submenu>
      <el-submenu index="universitySupervisors" v-if="hasRole(['admin'])">
        <template #title>
          <i class="el-icon-s-custom"></i>
          Opiekunowie
        </template>
        <el-menu-item index="university-supervisors/list"
          >Lista opiekunów</el-menu-item
        >
        <el-menu-item index="university-supervisors/add"
          >Dodaj opiekuna</el-menu-item
        >
      </el-submenu>
      <el-submenu
        index="studentGroups"
        v-if="hasRole(['admin', 'university_supervisor', 'company_supervisor'])"
      >
        <template #title>
          <i class="el-icon-user"></i>
          Studenci
        </template>
        <el-menu-item
          index="admin/students/list"
          v-if="
            hasRole(['admin', 'university_supervisor', 'company_supervisor'])
          "
          >Lista studentów</el-menu-item
        >
        <el-submenu index="student-group">
          <template #title>
            Grupy studenckie
          </template>
          <el-menu-item index="admin/student-groups/list"
            >Lista grup</el-menu-item
          >
          <el-menu-item
            index="admin/student-groups/add"
            v-if="hasRole(['university_supervisor'])"
            >Dodaj grupę</el-menu-item
          >
        </el-submenu>
      </el-submenu>
      <el-submenu index="companies" v-if="hasRole(['any'])">
        <template #title>
          <i class="el-icon-office-building"></i>
          Firmy
        </template>
        <el-menu-item index="companies/list">Lista firm</el-menu-item>
        <el-menu-item
            index="companies/add"
            v-if="hasRole(['university_supervisor', 'student', 'admin'])"
        >Dodaj firmę
        </el-menu-item>
        <el-submenu index="companySupervisors">
          <template #title>
            Opiekunowie
          </template>
          <el-menu-item index="company-supervisors/list"
            >Lista opiekunów</el-menu-item
          >
          <el-menu-item
              index="company-supervisors/add"
              v-if="hasRole(['university_supervisor', 'student', 'admin'])"
            >Dodaj opiekuna</el-menu-item
          >
        </el-submenu>
      </el-submenu>
      <el-menu-item index="stats">
        <i class="el-icon-sort"></i>
        Statystyki
      </el-menu-item>
      <el-menu-item index="info">
        <i class="el-icon-info"></i>
        O systemie
      </el-menu-item>
    </el-menu>
  </div>
</template>
<script>
export default {
  name: "main-menu",
  data() {
    return {
      activeIndex: "1",
      activeIndex2: "1",
      userLogged: false,
      drawerVisible: true,
      drawer: true,
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
      this.$router.push("/" + key);
    },
    handleLogin() {
      console.log("login");
      this.drawerVisible = true;
    },
    handleLoggedUser(data) {
      this.userLogged = true;
      console.log(data);
    },
    handleNotLoggedUser(data) {
      this.userLogged = false;
      console.log(data);
    },
    handleCloseDrawer() {
      this.drawerVisible = false;
    },
  },
  mounted() {
    this.drawerVisible = false;
  },
  computed: {
    userRole() {
      console.log("menu", this.$store.state.userData.roles);
      return this.$store.state.userData.roles;
    },
  },
};
</script>
<style>
.el-menu-demo {
  text-align: left;
}
.userarea {
  float: right;
}
</style>
