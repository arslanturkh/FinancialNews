import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ApiService, SearchParams } from '../app/services/api.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  @Output() searchSubmit = new EventEmitter<any>();

  searchWord: string = '';
  date: string = '';
  source: string = '';

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {}

  onSubmit(): void {
    const searchParams = {
      searchWord: this.searchWord,
      sourceSearch: this.source,
      date: this.date
    };

    this.searchSubmit.emit(searchParams);
    this.apiService.getNews(searchParams).subscribe(news => {
      // Process and display the articles
    });
  }
}
