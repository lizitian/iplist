#include<stdio.h>
char result[536870912];
int lg(unsigned int a)
{
    unsigned int ret = 0, k = 0x00000001;
    while(!(a & k)){
        k <<= 1;
        ret++;
    }
    return ret;
}
int lg2(unsigned int a)
{
    unsigned int ret = 31, k = 0x80000000;
    while(!(a & k)) {
        ret--;
        k >>= 1;
    }
    return ret;
}
int main(void)
{
    char buffer[1000];
    unsigned int i, a, b, c, d, len;
    while(fgets(buffer, 999, stdin) != NULL) {
    if(buffer[0] == '#') continue;
    i = 0;
    while(buffer[i++] != '|');
    while(buffer[i++] != '|');
    while(buffer[i++] != '|');
        sscanf(&buffer[i], "%d.%d.%d.%d|%d", &a, &b, &c, &d, &len);
    if(len % 8)
        return 10;
        for(i = a * 256 * 256 * 32 + b * 256 * 32 + c * 32 + d / 8; i < a * 256 * 256 * 32 + b * 256 * 32 + c * 32 + d / 8 + len / 8; i++)
            result[i] = 1;
    }
    unsigned int begin, k, l;
    i = 0;
    while(1) {
        while(result[++i] != 1) if(i >= 536870912) return 0;
        begin = i;
        while(result[++i] != 0);
        a = begin / 256 / 256 / 32;
        b = (begin % ( 256 * 256 * 32)) / 256 / 32;
        c = (begin % (256 * 32)) / 32;
        d = (begin % 32) * 8;
    e:
        begin = (((a * 256 + b) * 256) + c) * 32 + d / 8;
        k = 0x00000001;
        while(!(l = begin & k))
            k <<= 1;
        l *= 8;
        len = (i - begin) * 8;
        if(len > l) {
        printf("%d.%d.%d.%d/%d\n", a, b, c, d, 32 - lg(l));
    g:
        len -= l;
        a += l / 256 / 256 / 256;
        b += (l % (256 * 256 * 256)) / 256 / 256;
        c += (l % (256 * 256)) / 256;
        d += l % 256;
        if(d >= 256) {
            d = 0;
            c += 1;
        }
        if(c >= 256) {
            c -= 256;
            b += 1;
        }
        if(b >= 256) {
            b -= 256;
            a += 1;
        }
        goto e;
        } else {
            l = lg2(len);
            printf("%d.%d.%d.%d/%d\n", a, b, c, d, 32 - l);
            l = 0x00000001 << l;
            if(len - l != 0) 
                goto g;
        }
    }
    return 1;
}
