#!/bin/bash

for i in $(seq 1 10);
do
    java -XX:FreqInlineSize=$i JavaInlining
done