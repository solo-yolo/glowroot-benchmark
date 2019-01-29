How to build docker image:
---

`docker build --build-arg newrelic_license='SECRET' --build-arg stackify_license='SECRET' -t image-name .` 

Intermediate Results
---

### Glowroot

```
                               baseline      glowroot         delta
Response time (avg)           76.660 µs     81.146 µs      4.486 µs
Response time (50th)          73.472 µs     75.520 µs      2.048 µs
Response time (95th)          90.240 µs    104.320 µs     14.080 µs
Response time (99th)         148.992 µs    165.120 µs     16.128 µs
Response time (99.9th)       222.976 µs    233.984 µs     11.008 µs
Response time (99.99th)     2101.248 µs   2551.808 µs    450.560 µs
Allocation memory per req  46323.960 kb  48676.903 kb   2352.943 kb
Startup time (avg)          1773.123 ms   2707.715 ms    934.591 ms
```


### NewRelic
```
                               baseline      newrelic         delta
Response time (avg)           76.660 µs    142.387 µs     65.727 µs
Response time (50th)          73.472 µs    137.728 µs     64.256 µs
Response time (95th)          90.240 µs    158.208 µs     67.968 µs
Response time (99th)         148.992 µs    178.688 µs     29.696 µs
Response time (99.9th)       222.976 µs    391.680 µs    168.704 µs
Response time (99.99th)     2101.248 µs   5922.816 µs   3821.568 µs
Allocation memory per req  46323.960 kb  73483.647 kb  27159.687 kb
Startup time (avg)          1773.123 ms   3543.344 ms   1770.221 ms
```

### Stackify Retrace
```
                               baseline      stackify         delta
Response time (avg)           76.660 µs    130.857 µs     54.197 µs
Response time (50th)          73.472 µs    126.592 µs     53.120 µs
Response time (95th)          90.240 µs    147.712 µs     57.472 µs
Response time (99th)         148.992 µs    164.608 µs     15.616 µs
Response time (99.9th)       222.976 µs    413.696 µs    190.720 µs
Response time (99.99th)     2101.248 µs   2547.712 µs    446.464 µs
Allocation memory per req  46323.960 kb 171285.456 kb 124961.497 kb
Startup time (avg)          1773.123 ms   3294.160 ms   1521.037 ms

```
