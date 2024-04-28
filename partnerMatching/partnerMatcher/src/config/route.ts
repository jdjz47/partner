import Index from "../pages/Index.vue";
import Team from "../pages/Team.vue";
import searchPage from "../pages/searchPage.vue";
import UserEdits from "../pages/UserEdits.vue"
import UserPages from "../pages/UserPages.vue"
import searchResult from "../pages/searchResult.vue"
import TeamAddPage from "../pages/Team/TeamAddPage.vue"
import TeamUpdatePage from "../pages/Team/TeamUpdatePage.vue";
import PersonPage from "../pages/PersonPage.vue";
const routes = [
    { path:"/",redirect:"/Index"},
    { path: '/Index', component: Index },
    { path: '/Team', component: Team },
    { path: '/search', component: searchPage },
    { path: '/UserEdit',name:'/UserEdit', component: UserEdits },
    { path: '/UserPage', component: UserPages },
    { path: '/Person', component: PersonPage},
    { path: '/result',name:'/result', component: searchResult },
    { path: '/teamAdd',name:'/teamAdd', component: TeamAddPage},
    { path: '/updateTeam',name:'/updateTeam', component: TeamUpdatePage}
]
export default routes;