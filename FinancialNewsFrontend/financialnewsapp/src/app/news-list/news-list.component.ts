import { Component } from '@angular/core';
import { ApiService, News } from '../app/services/api.service';
import * as FusionCharts from 'fusioncharts';
import * as Charts from 'fusioncharts/fusioncharts.charts';
import * as FusionTheme from 'fusioncharts/themes/fusioncharts.theme.fusion';
import { FusionChartsModule } from 'angular-fusioncharts';

FusionChartsModule.fcRoot(FusionCharts, Charts, FusionTheme);

@Component({
  selector: 'app-news-list',
  templateUrl: './news-list.component.html',
  styleUrls: ['./news-list.component.css']
})
export class NewsListComponent {

  constructor(private apiService: ApiService) { }

  news: News[] = [];
  chartData: any[] = [];
  sort: { column: string, direction: 'asc' | 'desc' } = { column: 'title', direction: 'asc' };

  chartConfig: any = {
    type: 'column2d',
    width: '100%',
    height: '400',
    dataFormat: 'json',
    dataSource: {
      chart: {
        caption: 'Number of News Articles per Day',
        yAxisName: 'Number of Articles',
        xAxisName: 'Date',
        theme: 'fusion'
      },
      data: this.chartData
    }
  };

  fetchArticles(searchParams: any): void {
    this.apiService.getNews(searchParams).subscribe((data: News[]) => {
      this.news = data;
      this.sortNews();
      this.chartData = this.prepareChartData(data);
      this.chartConfig.dataSource.data = this.chartData;
    },
    (error) => {
      console.error('Error fetching data:', error);
    });
  }

  onSort(column: string): void {
    if (this.sort.column === column) {
      this.sort.direction = this.sort.direction === 'asc' ? 'desc' : 'asc';
    } else {
      this.sort.column = column;
      this.sort.direction = 'asc';
    }
  
    this.sortNews();
  }

  sortNews(): void {
    this.news.sort((a: News, b: News) => {
      const valueA = a[this.sort.column as keyof News];
      const valueB = b[this.sort.column as keyof News];

      if (valueA < valueB) {
        return this.sort.direction === 'asc' ? -1 : 1;
      }
      if (valueA > valueB) {
        return this.sort.direction === 'asc' ? 1 : -1;
      }
      return 0;
    });
  }
  
  getSortClass(column: string): string {
    return column === this.sort.column ? `sort-${this.sort.direction}` : '';
  }

  prepareChartData(news: News[]): any[] {
    const newsCountPerDay: { [date: string]: number } = {};
  
    news.forEach(article => {
      const date = new Date(article.publishedDate).toLocaleDateString();
      if (newsCountPerDay[date]) {
        newsCountPerDay[date]++;
      } else {
        newsCountPerDay[date] = 1;
      }
    });

    const sortedData = Object.entries(newsCountPerDay)
      .sort(([labelA, valueA], [labelB, valueB]) => {
        const dateA = new Date(labelA);
        const dateB = new Date(labelB);
        return dateA.getTime() - dateB.getTime();
      });
  
    return sortedData.map(([label, value]) => ({ label, value }));
  }
  
}
