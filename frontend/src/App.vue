<template>
  <header>
    <router-link to="/">
      <img class="logo" src="./assets/graduation.png" />
    </router-link>
    SWOPS
  </header>

  <el-container style="height: 100%; border: 1px solid #eee">

    <div class="mobile">
      <div class="btn-place">
        <el-button icon="el-icon-menu" @click="drawer = true" ></el-button>
      </div>
    <el-drawer
            title="Menu"
            v-model="drawer"
            direction="ltr"
            size="60%"
            :before-close="handleClose">
      <main-menu></main-menu>
    </el-drawer>
    </div>

    <el-aside class="desktop" width="200px" style="background-color: rgb(238, 241, 246)">
      <main-menu></main-menu>
    </el-aside>



    <el-container class="content">
      <router-view />
    </el-container>
  </el-container>
  <el-container style="height: 100%;">
    <el-container class="footer">
      <el-row>
        <el-col :span="12" class="left">
        System wspomagający obsługę praktyk studenckich - Projekt inżynierski
        </el-col>
        <el-col :span="12" class="right">
        Autorzy: Igor Dzierwa, Konrad Makuch, Adrian Nędza<br>
        Kierunek studiów: Informatyka(niestacjonarne)
        </el-col>
      </el-row>

    </el-container>

  </el-container>
  <el-container class="downfooter">
    <el-row>
      <el-col :span="24">
        <img width="70" src="./assets/agh_znk_pzt_rgb_150ppi.jpg" />
      </el-col>
    </el-row>
  </el-container>

</template>

<style lang="scss" src="./App.scss" rel="stylesheet/scss"></style>
<script>
import MainMenu from "./components/Menu";
export default {
  components: { MainMenu },
  data() {
    return {
      drawer: true
    };
  },
  methods: {
    checkIfLogged() {
      console.log("checking logged user");
      this.axios
              .get(this.api + "/logged/session")
              .then((resp) => {
                console.log("here OK!");
                this.$store.state.userData = resp.data;
                //this.$router.push("/your-account/");
              })
              .catch(() => {
                console.info("User not logged, can be registered...");
              });
    },

  },
  mounted(){
    this.checkIfLogged();
  }
};
</script>

<style lang="scss">

.content, .footer {
  padding: 30px;

  > .el-row {
    width: 100%;
  }
}

.downfooter{
  text-align: center;
  > .el-row {
    width: 100%;
  }
}
.footer{
  border-top: 0;
  font-size: 12px;
}

  .right{
    text-align: right;
  }
  .left{
    text-align: left;
  }

  .mobile{
    display: none;


  }

  .desktop{
    display: block;
  }


  @media (max-width: 767px){

    .btn-place{
      position: absolute;
      top: 40px;
      right: 30px;
    }
    .mobile{
      display: block;
    }
    .desktop{
      display: none;
    }
  }
  @media (max-width: 450px){
    .btn-place{
      top:10px;

    }
    .logo{
      max-width: 40px;
    }
  }
</style>
