import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }
  
  public getNews(searchParams: any): Observable<any> {
    const params = new HttpParams({ fromObject: searchParams });
    return this.http.get<any[]>(`${this.apiUrl}/api/news/search`, { params });
  }
}

export interface News {
  id: number;
  title: string;
  publishedDate: string;
  source: string;
  url: URL;
  fullTextSearchScore: number;
}

export interface SearchParams {
  searchWord?: string;
  sourceSearch?: string;
  date?: string;
}
