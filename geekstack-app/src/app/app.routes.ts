import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BoosterListComponent } from './features/cardlist/component/booster-list/booster-list.component';
import { CardListDisplayComponent } from './features/cardlist/component/card-list-display/card-list-display.component';
import { DeckbuilderComponent } from './features/deck/component/deckbuilder/deckbuilder.component';
import { HomeContentComponent } from './home-content/home-content.component';
import { DeckstacksComponent } from './features/post/component/deckstacks/deckstacks.component';
import { UserdetailsComponent } from './features/account/component/userdetails/userdetails.component';
import { RouteGuardService } from './core/service/route-guard.service';
import { PostcontentComponent } from './features/post/component/postcontent/postcontent.component';
import { NotificationsComponent } from './shared/component/notifications/notifications.component';
import { RegisterComponent } from './shared/component/register/register.component';

export const routes: Routes = [
  { path: '', component: HomeContentComponent },
  { path: 'home', component: HomeContentComponent },
  {path:'register', component:RegisterComponent},  
  { path: 'tcg/:tcg', component: BoosterListComponent},
  { path: 'tcg/:tcg/:booster', component: CardListDisplayComponent },
  { path: 'deckbuilder/:tcg', component: DeckbuilderComponent, canActivate:[RouteGuardService]},
  { path: 'poststacks', component: PostcontentComponent, canActivate: [RouteGuardService]},
  { path: 'stacks', component: DeckstacksComponent },
  { path: 'stacks/:postId', component: DeckstacksComponent },
  { path: 'account', component: UserdetailsComponent, canActivate: [RouteGuardService] },
  { path: 'notifications', component: NotificationsComponent, canActivate: [RouteGuardService] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
