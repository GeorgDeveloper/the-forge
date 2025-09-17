import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faHome,
  faUsers,
  faUsersCog,
  faCog,
  faCogs,
  faChevronDown,
  faChevronRight,
  faTasks,
  faCalendar,
  faShield,
  faGraduationCap,
  faSignOutAlt,
  faBook,
  faUser,
  faList,
  faFileAlt,
  faTachometerAlt,
  faHeart,
  faFileContract,
  faBuilding,
} from '@fortawesome/free-solid-svg-icons';

import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateDirective } from 'app/shared/language';

@Component({
  selector: 'jhi-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, FontAwesomeModule, TranslateModule, TranslateDirective],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit {
  faHome = faHome;
  faUsers = faUsers;
  faUsersCog = faUsersCog;
  faCog = faCog;
  faChevronDown = faChevronDown;
  faChevronRight = faChevronRight;
  faTasks = faTasks;
  faCalendar = faCalendar;
  faShield = faShield;
  faGraduationCap = faGraduationCap;
  faSignOutAlt = faSignOutAlt;
  faBook = faBook;
  faUser = faUser;
  faList = faList;
  faFileAlt = faFileAlt;
  faTachometerAlt = faTachometerAlt;
  faHeart = faHeart;
  faCogs = faCogs;
  faFileContract = faFileContract;
  faBuilding = faBuilding;

  inProduction?: boolean;
  isNavbarCollapsed = true;
  openAPIEnabled?: boolean;
  version = '';
  account = inject(AccountService).trackCurrentAccount();
  entitiesNavbarItems: any[] = [];

  private loginService = inject(LoginService);
  private profileService = inject(ProfileService);
  private router = inject(Router);

  // Состояние раскрытых меню
  expandedMenus: { [key: string]: boolean } = {
    entities: false,
    admin: false,
  };

  constructor() {
    // VERSION будет добавлен позже
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
  }

  toggleMenu(menuKey: string): void {
    this.expandedMenus[menuKey] = !this.expandedMenus[menuKey];
  }

  isMenuExpanded(menuKey: string): boolean {
    return this.expandedMenus[menuKey] || false;
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  getImageUrl(): string {
    return this.inProduction ? 'content/images/logo-forge.png' : 'content/images/logo-forge.png';
  }
}
