How to build docker image:
---

`docker build --build-arg newrelic_license='SECRET' --build-arg stackify_license='SECRET' -t image-name .` 

Intermediate Results
---

### Glowroot

```
                               baseline      glowroot         delta
Response time (avg)           76.680 µs     86.689 µs     10.009 µs
Response time (50th)          73.344 µs     79.488 µs      6.144 µs
Response time (95th)          91.520 µs    120.576 µs     29.056 µs
Response time (99th)         151.808 µs    176.640 µs     24.832 µs
Response time (99.9th)       228.352 µs    235.264 µs      6.912 µs
Response time (99.99th)     2113.536 µs   2674.688 µs    561.152 µs
Allocation memory per req  46292.701 kb  49802.887 kb   3510.185 kb
Startup time (avg)          1774.189 ms   2856.403 ms   1082.214 ms
```


### NewRelic
```
                               baseline      newrelic         delta
Response time (avg)           76.680 µs    142.421 µs     65.741 µs
Response time (50th)          73.344 µs    137.728 µs     64.384 µs
Response time (95th)          91.520 µs    158.208 µs     66.688 µs
Response time (99th)         151.808 µs    178.688 µs     26.880 µs
Response time (99.9th)       228.352 µs    386.048 µs    157.696 µs
Response time (99.99th)     2113.536 µs   6127.616 µs   4014.080 µs
Allocation memory per req  46292.701 kb  73351.224 kb  27058.523 kb
Startup time (avg)          1774.189 ms   3852.715 ms   2078.526 ms
```

### Stackify Retrace
```
                               baseline      stackify         delta
Response time (avg)           76.680 µs    130.586 µs     53.906 µs
Response time (50th)          73.344 µs    126.720 µs     53.376 µs
Response time (95th)          91.520 µs    147.200 µs     55.680 µs
Response time (99th)         151.808 µs    162.560 µs     10.752 µs
Response time (99.9th)       228.352 µs    392.282 µs    163.930 µs
Response time (99.99th)     2113.536 µs   2514.944 µs    401.408 µs
Allocation memory per req  46292.701 kb 171685.110 kb 125392.409 kb
Startup time (avg)          1774.189 ms   3292.869 ms   1518.680 ms
```
