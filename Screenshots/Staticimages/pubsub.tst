* (C) Nastel Technologies, Inc. 1997 - 2011
*
* File: pubsubl.tst for MQ 7 and later
*
* This MQSC file is required by M6-WMQ Publish-
* Subscribe Server. Apply this file to all 
* queue managers. Before running this script 
* make sure [APWMQ_HOME]\mqsc\nastel.tst has 
* already been applied.

DEFINE QLOCAL('NASTEL.PUBSUB.EVENT.QUEUE') +
DESCR('AP-WMQ publish-subscriber event queue') +
MAXDEPTH(100000) +
REPLACE

DEFINE QALIAS(SYSTEM.ADMIN.QMGR.EVENT) +
TARGTYPE(QUEUE) TARGET(NASTEL.PUBSUB.EVENT.QUEUE) +
REPLACE

DEFINE QALIAS(SYSTEM.ADMIN.CHANNEL.EVENT) +
TARGTYPE(QUEUE) TARGET(NASTEL.PUBSUB.EVENT.QUEUE) +
REPLACE

DEFINE QALIAS(SYSTEM.ADMIN.PERFM.EVENT) +
TARGTYPE(QUEUE) TARGET(NASTEL.PUBSUB.EVENT.QUEUE) +
REPLACE

DEFINE QALIAS(SYSTEM.ADMIN.CONFIG.EVENT) +
TARGTYPE(QUEUE) TARGET(NASTEL.PUBSUB.EVENT.QUEUE) +
REPLACE

DEFINE QALIAS(SYSTEM.ADMIN.COMMAND.EVENT) +
TARGTYPE(QUEUE) TARGET(NASTEL.PUBSUB.EVENT.QUEUE) +
REPLACE

DEFINE QALIAS(SYSTEM.ADMIN.LOGGER.EVENT) +
TARGTYPE(QUEUE) TARGET(NASTEL.PUBSUB.EVENT.QUEUE) +
REPLACE

DEFINE QALIAS(SYSTEM.ADMIN.BRIDGE.EVENT) +
TARGTYPE(QUEUE) TARGET(NASTEL.PUBSUB.EVENT.QUEUE) +
REPLACE

* ***********************************************
* Accounting events can be very high volume,
* do not enable it without neccessity.
* DEFINE QALIAS(SYSTEM.ADMIN.ACCOUNTING.QUEUE) +
* TARGTYPE(QUEUE) TARGET(NASTEL.PUBSUB.EVENT.QUEUE) +
* REPLACE
* ***********************************************

DEFINE QALIAS(SYSTEM.ADMIN.STATISTICS.QUEUE) +
TARGTYPE(QUEUE) TARGET(NASTEL.PUBSUB.EVENT.QUEUE) +
REPLACE
