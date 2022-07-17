from kafka import KafkaConsumer, TopicPartition
import time
import json
import requests

# change the broker information here
TOPIC = 'Thingsboard'
GROUP = 'python1'
BOOTSTRAP_SERVERS = ['13.251.166.96:9092']
LOG_SERVER = 'http://13.251.166.96/api/v1/VUsTDQL36fZTknuxEdDG/telemetry'
CHECK_FREQUENCY = 60

# init the consumer connection
# disable the auto commit to avoid influencing polling process
consumer = KafkaConsumer(
        bootstrap_servers=BOOTSTRAP_SERVERS,
        group_id=GROUP,
        enable_auto_commit=False
    )

while True:
    try:
        # get the TopicPartition information from the assigned topic
        for p in consumer.partitions_for_topic(TOPIC):
            tp = TopicPartition(TOPIC, p)
            consumer.assign([tp])
            committed = consumer.committed(tp)
            consumer.seek_to_end(tp)
            last_offset = consumer.position(tp)
            # lag = latest offset - committed offset
            print("topic: %s partition: %s committed: %s last: %s lag: %s" % (TOPIC, p, committed, last_offset, (last_offset - committed)))

            '''
            TODO
            generate the alarm if
            1. lag is larger than the polling throughput
            2. current lag is larger than last lag
            3. lag is continuously increasing
            '''
            # post the lag to Thingsboard for logging，可以查看延迟
            url = 'http://13.251.166.96/api/v1/VUsTDQL36fZTknuxEdDG/telemetry'
            myobj = {'lag': (last_offset - committed)}
            print(json.dumps(myobj))
            headers = {'content-type': 'application/json'}
            x = requests.post(url, data = json.dumps(myobj), headers=headers)
            print(x.text)
        # monitor the lag every 60s
        time.sleep(CHECK_FREQUENCY)
    except Exception as e:
        print (e)
    except (KeyboardInterrupt, SystemExit):
        raise
