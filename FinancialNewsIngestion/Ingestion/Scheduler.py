import schedule
import time
from IngestNews import ingestDailyNews

# Schedule the ingestDailyNews function to run daily at a specific time, e.g., 1:00 AM
schedule.every().day.at("01:00").do(ingestDailyNews)


# Keep the script running indefinitely to allow the scheduler to execute the job
def runScheduler():
    while True:
        schedule.run_pending()
        time.sleep(60)  # Wait for 60 seconds before checking again


if __name__ == "__main__":
    runScheduler()