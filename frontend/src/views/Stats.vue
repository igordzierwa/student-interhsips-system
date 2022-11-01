<template>
  <div class="full">
    <el-row>
      <el-col :span="12">
        <el-card class="box-card">
          <h4>Liczba studentów w systemie: {{ model.data.studentCount }}</h4>
          Wybierz rok:
          <el-select
            v-model="stats.student.year"
            @change="handleYearChange('student')"
          >
            <el-option
              v-for="item in model.years"
              :key="item"
              :label="item"
              :value="item"
            >
            </el-option>
          </el-select>
          <students-chart
            main_label="Liczba nowych studentów"
            :data="stats.student.data"
            :mth="model.mthNames"
            ref="chart_student"
          ></students-chart>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <h4>
            Liczba Opiekunów firmowych w systemie: {{ model.data.csCount }}
          </h4>
          Wybierz rok:
          <el-select
            v-model="stats.company_supervisor.year"
            @change="handleYearChange('company_supervisor')"
          >
            <el-option
              v-for="item in model.years"
              :key="item"
              :label="item"
              :value="item"
            >
            </el-option>
          </el-select>
          <students-chart
            main_label="Liczba nowych opiekunów"
            :data="stats.company_supervisor.data"
            :mth="model.mthNames"
            ref="chart_company_supervisor"
          ></students-chart>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <h4>
            Liczba Opiekunów z uczelni w systemie: {{ model.data.usCount }}
          </h4>
          Wybierz rok:
          <el-select
            v-model="stats.university_supervisor.year"
            @change="handleYearChange('university_supervisor')"
          >
            <el-option
              v-for="item in model.years"
              :key="item"
              :label="item"
              :value="item"
            >
            </el-option>
          </el-select>
          <students-chart
            main_label="Liczba nowych opiekunów"
            :data="stats.university_supervisor.data"
            :mth="model.mthNames"
            ref="chart_university_supervisor"
          ></students-chart>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="box-card">
          <h4>Liczba administratorów systemie: {{ model.data.adminCount }}</h4>
          Wybierz rok:
          <el-select
            v-model="stats.admin.year"
            @change="handleYearChange('admin')"
          >
            <el-option
              v-for="item in model.years"
              :key="item"
              :label="item"
              :value="item"
            >
            </el-option>
          </el-select>
          <students-chart
            main_label="Liczba nowych adminów"
            :data="stats.admin.data"
            :mth="model.mthNames"
            ref="chart_admin"
          ></students-chart>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="box-card">
          <h4>Liczba wydziałów: {{ model.data.departmentCount }}</h4>

          <students-chart
            main_label="Najpopularniejsze wydziały (ilość przypisanych studentów)"
            :data="stats.departments.data"
            :mth="stats.departments.labels"
            ref="chart_departments"
          ></students-chart>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="box-card">
          <h4>Liczba firm: {{ model.data.companyCount }}</h4>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import StudentsChart from "../components/charts/StudentsChart";

export default {
  name: "StudentsList",
  components: {
    StudentsChart,
  },
  data() {
    return {
      componentName: "Styats",
      datacollection: null,
      model: {
        list: [],
        data: {},
        toDeleteId: -1,
        dialogVisible: false,
        years: [2021, 2022, 2023, 2024, 2025],
        mthNames: [
          "Styczeń",
          "Luty",
          "Marzec",
          "Kwiecień",
          "Maj",
          "Czerwiec",
          "Lipiec",
          "Sierpień",
          "Wrzesień",
          "Październik",
          "Listopad",
          "Grudzień",
        ],
      },
      stats: {
        student: {
          year: 2021,
          data: [],
        },
        company_supervisor: {
          year: 2021,
          data: [],
        },
        university_supervisor: {
          year: 2021,
          data: [],
        },
        admin: {
          year: 2021,
          data: [],
        },
        departments: {
          data: [],
          labels: [],
        },
        companies: {
          year: 2021,
          data: [],
        },
      },
      studentYear: 2021,
      rules: {
        name: [{ required: true, message: "Pole wymagane", trigger: "change" }],
        surname: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
        department: [
          { required: true, message: "Pole wymagane", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    loadDefaultStats() {
      this.axios({
        method: "GET",
        url: this.api + "/defaultStats",
      })
        .then((resp) => {
          console.log(resp);
          this.model.data = resp.data;
        })
        .catch((error) => {
          this.$message({
            message: "Błąd pobierania. Error: " + error.data.message,
            type: "error",
          });
        });
    },
    handleYearChange(type) {
      this.axios({
        method: "GET",
        url: this.api + "/stats/" + type + "/" + this.stats[type].year,
      })
        .then((resp) => {
          this.stats[type].data = resp.data;
          setTimeout(() => {
            this.$refs["chart_" + type].refresh();
          }, 100);
        })
        .catch((error) => {
          this.$message({
            message: "Błąd pobierania. Error: " + error.data.message,
            type: "error",
          });
        });
    },

    triggerRefreshDepartmentStat() {
      this.axios({
        method: "GET",
        url: this.api + "/stats-departments/",
      })
        .then((resp) => {
          this.stats["departments"].data = [];
          this.stats["departments"].labels = [];

          resp.data.forEach((item) => {
            this.stats["departments"].data.push(item.total);
            this.stats["departments"].labels.push(item.shortname);
          });

          setTimeout(() => {
            this.$refs["chart_departments"].refresh();
          }, 100);
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
    this.loadDefaultStats();

    this.handleYearChange("student");
    this.handleYearChange("company_supervisor");
    this.handleYearChange("university_supervisor");
    this.handleYearChange("admin");
    this.triggerRefreshDepartmentStat();
  },
};
</script>
<style>
.box-card {
  text-align: left;
}
</style>
