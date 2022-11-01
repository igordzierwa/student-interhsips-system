import { createRouter, createWebHistory } from "vue-router";

import Home from "../views/Home.vue";

const routes = [
  { path: "/", name: "Home", component: Home },
  {
    path: "/your-account",
    name: "Your account",
    component: () => import("../views/YourAccount/YourAccount.vue"),
  },

  {
    path: "/departments/list",
    name: "DepartmentsList",
    component: () => import("../views/Departments/List.vue"),
  },
  {
    path: "/departments/:id",
    name: "DepartmentsEdit",
    component: () => import("../views/Departments/Add.vue"),
    props: { edit: true },
  },
  {
    path: "/departments/add",
    name: "DepartmentsAdd",
    component: () => import("../views/Departments/Add.vue"),
  },

  {
    path: "/university-supervisors/add",
    name: "USAdd",
    component: () => import("../views/UniversitySupervisors/Add.vue"),
  },
  {
    path: "/university-supervisors/:id",
    name: "USEdit",
    component: () => import("../views/UniversitySupervisors/Add.vue"),
    props: { edit: true },
  },
  {
    path: "/university-supervisors/list",
    name: "USList",
    component: () => import("../views/UniversitySupervisors/List.vue"),
  },
  {
    path: "/university-supervisors/activate",
    name: "USActivate",
    component: () => import("../views/UniversitySupervisors/Activate.vue"),
  },
    {
        path: "/university-supervisors/accept-internship",
        name: "Internship/acceptUS",
        component: () => import("../views/UniversitySupervisors/AcceptInternship.vue"),
    },

  {
    path: "/admin/students/list",
    name: "StudentsList",
    component: () => import("../views/Students/List.vue"),
  },
  {
    path: "/admin/students/:id",
    name: "StudentsEdit",
    component: () => import("../views/Students/Edit.vue"),
  },

  {
    path: "/admin/student-groups/list",
    name: "StudentGroups",
    component: () => import("../views/StudentGroups/List.vue"),
  },

  {
    path: "/admin/student-groups/:id/add-students",
    name: "AddStudentsToStudentGroup",
    component: () => import("../views/StudentGroups/AddStudents.vue"),
  },

  {
    path: "/companies/list",
    name: "CompaniesList",
    component: () => import("../views/Companies/List.vue"),
  },
  {
    path: "/companies/add",
    name: "CompaniesAdd",
    component: () => import("../views/Companies/Add.vue"),
  },
  {
    path: "/companies/:id",
    name: "CompaniesEdit",
    component: () => import("../views/Companies/Add.vue"),
    props: { edit: true },
  },
  {
    path: "/company-supervisors/activate",
    name: "CSActivate",
    component: () => import("../views/CompanySupervisors/Activate.vue"),
  },
  {
    path: "/company-supervisors/list",
    name: "CSAdd",
    component: () => import("../views/CompanySupervisors/List.vue"),
  },
  {
    path: "/company-supervisors/list/:id",
    name: "CSListFromComp",
    component: () => import("../views/CompanySupervisors/List.vue"),
  },
  {
    path: "/company-supervisors/add",
    name: "CSList",
    component: () => import("../views/CompanySupervisors/Add.vue"),
  },
  {
    path: "/company-supervisors/:id",
    name: "CSEdit",
    component: () => import("../views/CompanySupervisors/Add.vue"),
    props: { edit: true },
  },

  {
    path: "/register",
    name: "RegisterForm",
    component: () => import("../components/Register.vue"),
  },

  {
    path: "/student/your-practice",
    name: "Internship",
    component: () => import("../views/Internship/Internship.vue"),
  },
  {
    path: "/company-supervisors/accept-internship",
    name: "Internship/acceptCS",
    component: () => import("../views/CompanySupervisors/AcceptInternship.vue"),
  },


  {
    path: "/student/your-practice/create",
    name: "CreateInternship",
    component: () => import("../views/Internship/Create.vue")
  },

  {
    path: "/student/your-practice/details/:id",
    name: "InternshipDetails",
    component: () => import("../views/Internship/Details.vue")
  },

  {
    path: "/internships-to-accept",
    name: "ListOfInternshipsToBeAccepted",
    component: () => import("../views/Internship/ToBeAcceptedList.vue")
  },

  {
    path: "/admin/student-groups/add",
    name: "AddStudentGroup",
    component: () => import("../views/StudentGroups/Add.vue"),
  },

  {
    path: "/messages/received",
    name: "ReceivedMessageList",
    component: () => import("../views/Messages/ListReceivedMsg.vue"),
  },
  {
    path: "/messages/sent",
    name: "SentMessageList",
    component: () => import("../views/Messages/ListSentMsg.vue"),
  },
  {
    path: "/messages/new",
    name: "NewMessage",
    component: () => import("../views/Messages/NewMsg.vue"),
  },
  {
    path: "/messages/:id",
    name: "SpecificMessage",
    component: () => import("../views/Messages/SpecificMsg.vue"),
  },

  {
    path: "/info",
    name: "About",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue"),
  },
  {
    path: "/stats",
    name: "Stats",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/Stats.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
