#!/usr/bin/env python
# coding: utf-8

# In[14]:


import datetime
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
import mysql.connector
from pandas import Timestamp

# Connect to MySQL database
mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    database="flo_dbtfin"
)

# 1. Data Preprocessing
# Fetch events data from database
events_query = "SELECT id, nameevent, datedebut, image FROM evenement"
events_df = pd.read_sql(events_query, mydb)
# 2. Feature Engineering
# Convert the 'datedebut' column to datetime and extract relevant features
events_df['datedebut'] = pd.to_datetime(events_df['datedebut'])
# Convert today_date to a pandas Timestamp
today_date = Timestamp(datetime.datetime.now().date())

# 3. Model Training
# For content-based filtering, we'll use TF-IDF Vectorizer to convert text data into numerical vectors
tfidf = TfidfVectorizer(analyzer='word', ngram_range=(1, 3), stop_words='english')
event_matrix = tfidf.fit_transform(events_df['nameevent'])

# 4. Recommendation
# Calculate similarity between events based on their names
cosine_sim = linear_kernel(event_matrix, event_matrix)

# Function to recommend upcoming events
def recommend_events(today_date, cosine_sim=cosine_sim, events_df=events_df):
    events_df['days_until_event'] = (events_df['datedebut'] - today_date).dt.days
    upcoming_events = events_df[events_df['days_until_event'] > 0]
    if len(upcoming_events) == 0:
        return "No upcoming events."

    # Sort events by days until event
    upcoming_events = upcoming_events.sort_values(by='days_until_event')

    # Select top 3 upcoming events
    # Instead of returning the entire DataFrame, return only the IDs of the top 3 upcoming events
    top_event_ids = upcoming_events.head(3)[['id', 'days_until_event']].values.tolist()
    return top_event_ids

# Call the function and write the IDs to a file
top_event_ids = recommend_events(today_date)
with open('recommended_event_ids.txt', 'w') as f:
    for id in top_event_ids:
        f.write(f"{id}\n")
# Recommend upcoming events

#print("Top 3 upcoming events:")
#print(recommendations)


# In[ ]:





# In[ ]:





# In[ ]:




