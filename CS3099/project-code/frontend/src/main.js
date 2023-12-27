import { createApp } from "vue";
import router from "./router.js";
import App from "./App.vue";
import VueCookies from "vue-cookies";
import BootstrapVue from "bootstrap-vue-3";

import { library } from "@fortawesome/fontawesome-svg-core";
import {
  faRotateLeft,
  faPencil,
  faRightFromBracket,
  faXmark,
  faInfo,
  faArrowRight,
  faArrowLeft,
  faTrash,
  faUpload,
  faList,
  faCaretDown,
  faCaretUp,
  faComments,
  faDownload
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

const app = createApp(App);

library.add(
  faRotateLeft,
  faPencil,
  faRightFromBracket,
  faXmark,
  faInfo,
  faArrowRight,
  faArrowLeft,
  faTrash,
  faUpload,
  faList,
  faCaretDown,
  faCaretUp,
  faComments,
  faDownload
);

app.use(BootstrapVue);
app.use(router);
app.use(VueCookies);

app.component("font-awesome-icon", FontAwesomeIcon);

app.mount("#app");
