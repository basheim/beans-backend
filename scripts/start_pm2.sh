#!/bin/bash

pm2 kill
pm2 start ~/beans-backend/scripts/keep_db_open.sh --cron "0 * * * *"
pm2 start ~/beans-backend/scripts/update_plants.sh --cron "0 * * * *"