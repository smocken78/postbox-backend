import "../webcomponents/postbox-client.js";
import { WebcomponentRouter } from "../lib/webcomponent-router.js";
import "../lib/popper.js";
import "../lib/bootstrap.esm.js";

const routerTarget = document.querySelector('#router-target');
const routes = [
  {
    path: '/',
    component: 'postbox-main',
    action: async () => { await import("../webcomponents/postbox-main.js") }
  }
];

new WebcomponentRouter(routerTarget, routes);

window.onresize = () => {
  const evt = new CustomEvent("resizeWindow", { bubbles: true });
  dispatchEvent(evt);
};
