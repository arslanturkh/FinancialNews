import dateutil.utils
import psycopg2
import FinNews as fn
from datetime import datetime
from typing import List, Dict
from abc import ABC, abstractmethod
from sqlalchemy import create_engine, Table, Column, Integer, String, Date, MetaData
from sqlalchemy.orm import declarative_base, Session
from sqlalchemy.ext.declarative import declarative_base
from Models import News, Base


# NewsFetcher interface
class NewsFetcher(ABC):
    @abstractmethod
    def fetchNews(self) -> List[Dict[str, str]]:
        pass


# YahooNewsFetcher class
class YahooNewsFetcher(NewsFetcher):
    def __init__(self, topicFilter):
        self.news_client = fn.Yahoo(topics = topicFilter)

    def fetchNews(self):
        return self.news_client.get_news()


# CNBCNewsFetcher class
class CNBCNewsFetcher(NewsFetcher):
    def __init__(self, topicFilter):
        self.news_client = fn.CNBC(topics=topicFilter)

    def fetchNews(self):
        return self.news_client.get_news()


# WSJNewsFetcher class
class WSJNewsFetcher(NewsFetcher):
    def __init__(self, topicFilter):
        self.news_client = fn.WSJ(topics = topicFilter)

    def fetchNews(self):
        return self.news_client.get_news()


# InvestingNewsFetcher class
class InvestingNewsFetcher(NewsFetcher):
    def __init__(self, topicFilter):
        self.news_client = fn.Investing(topics = topicFilter)

    def fetchNews(self):
        return self.news_client.get_news()


# FinancialTimesNewsFetcher class
class FinancialTimesNewsFetcher(NewsFetcher):
    def __init__(self, topicFilter):
        self.news_client = fn.FT(topics = topicFilter)

    def fetchNews(self):
        return self.news_client.get_news()


# FortuneNewsFetcher class
class FortuneNewsFetcher(NewsFetcher):
    def __init__(self, topicFilter):
        self.news_client = fn.Fortune(topics = topicFilter)

    def fetchNews(self):
        return self.news_client.get_news()


# MarketWatchNewsFetcher class
class MarketWatchNewsFetcher(NewsFetcher):
    def __init__(self, topicFilter):
        self.news_client = fn.MarketWatch(topics = topicFilter)

    def fetchNews(self):
        return self.news_client.get_news()


# NasdaqNewsFetcher class
class NasdaqNewsFetcher(NewsFetcher):
    def __init__(self, topicFilter):
        self.news_client = fn.Nasdaq(topics = topicFilter)

    def fetchNews(self):
        return self.news_client.get_news()


# CNNMoneyNewsFetcher class
class CNNMoneyNewsFetcher(NewsFetcher):
    def __init__(self, topicFilter):
        self.news_client = fn.CNNMoney(topics = topicFilter)

    def fetchNews(self):
        return self.news_client.get_news()


# ReutersNewsFetcher class
class ReutersNewsFetcher(NewsFetcher):
    def __init__(self, topicFilter):
        self.news_client = fn.Reuters(topics = topicFilter)

    def fetchNews(self):
        return self.news_client.get_news()


# News fetcher factory method
def newsFetcherFactory(channel: str, topicFilter: List[str]) -> NewsFetcher:
    if channel.lower() == 'yahoo':
        return YahooNewsFetcher(topicFilter)
    elif channel.lower() == 'cnbc':
        return CNBCNewsFetcher(topicFilter)
    elif channel.lower() == 'wsj':
        return WSJNewsFetcher(topicFilter)
    elif channel.lower() == 'investing':
        return InvestingNewsFetcher(topicFilter)
    elif channel.lower() == 'financialtimes':
        return FinancialTimesNewsFetcher(topicFilter)
    elif channel.lower() == 'fortune':
        return FortuneNewsFetcher(topicFilter)
    elif channel.lower() == 'marketwatch':
        return MarketWatchNewsFetcher(topicFilter)
    elif channel.lower() == 'nasdaq':
        return NasdaqNewsFetcher(topicFilter)
    elif channel.lower() == 'cnnmoney':
        return CNNMoneyNewsFetcher(topicFilter)
    elif channel.lower() == 'reuters':
        return ReutersNewsFetcher(topicFilter)
    else:
        raise ValueError(f'Unsupported channel: {channel}')


def ingestDailyNews():
    # Replace the placeholders with your PostgreSQL database credentials
    db_url = "postgresql://{UserName}:{Password}@localhost:5432/{DBName}"
    engine = create_engine(db_url)
    Base.metadata.create_all(engine)
    session = Session(engine)

    yesterday = date.today() - timedelta(days=1)
    articles = []
    channels = ['Yahoo', 'CNBC', 'WSJ', 'investing', 'financialtimes', 'fortune', 'marketwatch', 'reuters']
    topicFilter = [
        'finance', 'financial', 'stock', 'stocks', 'market', 'markets', 'trading', 'invest', 'investment',
        'investing', 'economy', 'economic', 'recession', 'inflation', 'debt', 'interest', 'rate', 'rates',
        'bank', 'banks', 'banking', 'credit', 'loan', 'loans', 'mortgage', 'fintech', 'cryptocurrency',
        'bitcoin', 'ethereum', 'blockchain', 'earnings'
    ]

    for channel in channels:
        newsFetcher = newsFetcherFactory(channel, topicFilter)
        rawArticles = newsFetcher.fetchNews()

        for article in rawArticles:
            publishDate = datetime.strptime(article['published'], "%a, %d %b %Y %H:%M:%S %Z").date()

            if publishDate == yesterday:
                articles.append({
                    'title': article['title'],
                    'url': article['link'],
                    'source': channel,
                    'published_date': publishDate
                })

    session.bulk_insert_mappings(News, articles)
    session.commit()
    session.close()

#if __name__ == '__main__':
    #ingestDailyNews()