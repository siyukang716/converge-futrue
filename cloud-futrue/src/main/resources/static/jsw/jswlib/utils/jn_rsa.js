window.jnRSA = {};
(function (jnRSA) {

    //小素数表
    jnRSA.g_PrimeTable = [3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97];
    jnRSA.g_PrimeCount = jnRSA.g_PrimeTable.length;
    jnRSA.g_Rnd;


    jnRSA.RSA_PARAM = function () {
    };

    jnRSA.RSA_PARAM.prototype = {
        p: null,   //(Number)两个素数，不参与加密解密运算
        q: null,   //(Number)两个素数，不参与加密解密运算
        f: null,   //(Number)f=(p-1)*(q-1)，不参与加密解密运算
        n: null,
        e: null,   //(Number)公匙，n=p*q，gcd(e,f)=1
        d: null,   //(Number)私匙，e*d=1 (mod f)，gcd(n,d)=1
        s: null    //(Number)块长，满足2^s<=n的最大的s，即log2(n)
    };

    jnRSA.RandNumber = function (s/*Number*/) {
        if (!s || typeof (s) != "Number") {
            this.randSeed = parseInt(Math.random() * 1000000);
        } else {
            this.randSeed = s;
        }
    };

    jnRSA.RandNumber.prototype = {
        randSeed: null,

        Random: function (n/*Number*/) {
            var multiplier = 12747293821;
            var adder = 1343545677842234541;//随机数类

            var mult = jnRSA.Multiply(multiplier, this.randSeed);
            this.randSeed = (mult + adder) % adder;
            return this.randSeed % n;
        }
    };

    /*  Rabin-Miller素数测试，通过测试返回1，否则返回0。 n是待测素数。注意：通过测试并不一定就是素数，非素数通过测试的概率是1/4  */
    jnRSA.RabinMillerKnl = function (n/*Number*/) {
        var b, m, j, v, i, tmp = 2;
        m = n - 1;
        j = 0;    //0、先计算出m、j，使得n-1=m*2^j，其中m是正奇数，j是非负整数
        while (!(m & 1)) {
            ++j;
            m = parseInt(m / 2);
        }    //1、随机取一个b，2<=b<n-1
        b = 2 + jnRSA.g_Rnd.Random(n - 3);    //2、计算v=b^m mod n
        v = jnRSA.PowMod(b, m, n);    //3、如果v==1，通过测试
        if (v == 1) {
            return 1;
        }    //4、令i=1
        i = 1;    //5、如果v=n-1，通过测试
        while (v != n - 1) {
            //6、如果i==l，非素数，结束
            if (i == j) {
                return 0;            //7、v=v^2 mod n，i=i+1
            }
            v = jnRSA.PowMod(v, tmp, n);
            ++i;        //8、循环到5
        }
        return 1;
    };



    /*   Rabin-Miller素数测试，循环调用核心loop次全部通过返回1，否则返回0    */
    jnRSA.RabinMiller = function (n, loop) {
        //先用小素数筛选一次，提高效率
        for (i = 0; i < jnRSA.g_PrimeCount; i++) {
            if (n % jnRSA.g_PrimeTable[i] == 0) {
                return 0;
            }
        }    //循环调用Rabin-Miller测试loop次，使得非素数通过测试的概率降为(1/4)^loop
        for (i = 0; i < loop; i++) {
            if (!jnRSA.RabinMillerKnl(n)) {
                return 0;
            }
        }
        return 1;
    };



    /* 随机生成一个bits位(二进制位)的素数，最多32位 */
    jnRSA.RandomPrime = function (bits) {
        var base;
        do {
            base = 1 << (bits - 1);   //保证最高位是1
            base = base + jnRSA.g_Rnd.Random(base);               //再加上一个随机数
            base = base | 1;    //保证最低位是1，即保证是奇数
        } while (!jnRSA.RabinMiller(base, 30));    //进行拉宾－米勒测试30次

        return base;    //全部通过认为是素数
    };

    /*欧几里得法求最大公约数*/
    jnRSA.EuclidGcd = function (p, q) {
        var a = p > q ? p : q;
        var b = p < q ? p : q;
        var t;
        if (p == q) {
            return p;   //两数相等，最大公约数就是本身
        }
        else {
            while (b)    //辗转相除法，gcd(a,b)=gcd(b,a-qb)
            {
                a = a % b;
                t = a;
                a = b;
                b = t;
            }
            return a;
        }
    };

    jnRSA.SteinGcd = function (a, b) {
        if (a < b) {
            return jnRSA.SteinGcd(b, a);
        }
        else if (b == 0) {
            return a;
        }
        else {
            return jnRSA.SteinGcd(b, a % b);
        }
    };

    /*  Stein法求最大公约数  */
    jnRSA.SteinGcd2 = function (p, q) {
        var a = p > q ? p : q;
        var b = p < q ? p : q;
        var t, r = 1;
        if (p == q)
            return p;           //两数相等，最大公约数就是本身  }
        else {
            while ((!(a & 1)) && (!(b & 1))) {
                r = r * 2;          //a、b均为偶数时，gcd(a,b)=2*gcd(a/2,b/2)
                a = parseInt(a / 2);
                b = parseInt(b / 2);
            }
            if (!(a & 1)) {
                t = a;            //如果a为偶数，交换a，b
                a = b;
                b = t;
            }
            do {
                while (!(b & 1))
                    b = parseInt(b / 2); //b为偶数，a为奇数时，gcd(b,a)=gcd(b/2,a)   }  
                if (b < a) {
                    t = a;        //如果b小于a，交换a，b
                    a = b;
                    b = t;
                }
                b = parseInt((b - a) / 2); //b、a都是奇数，gcd(b,a)=gcd((b-a)/2,a)
            } while (b);
            return jnRSA.Multiply(r, a);
        }
    };



    /*  已知a、b，求x，满足a*x =1 (mod b)相当于求解a*x-b*y=1的最小整数解   */
    jnRSA.Euclid = function (a, b) {
        var m, e, i, j, x, y;
        var xx, yy;
        m = b;
        e = a;
        x = 0;
        y = 1;
        xx = 1;
        yy = 1;

        var times = 1;
        while (e) {
            i = parseInt(m / e);
            j = m % e;
            m = e;
            e = j;
            j = y;
            y = j * i;
            if (xx == yy) {
                if (x > y) {
                    y = x - y;
                } else {
                    y = y - x;
                    yy = 0;
                }
            } else {
                y = y + x;
                xx = 1 - xx;
                yy = 1 - yy;
            }
            x = j;
        }
        if (xx == 0)
            x = b - x;
        return x;
    };


    /* 随机产生一个RSA加密参数 (e, d, n) */
    jnRSA.RsaGetParam = function () {
        var Rsa = new jnRSA.RSA_PARAM();
        var t;
        Rsa.p = jnRSA.RandomPrime(16);          //随机生成两个素数
        Rsa.q = jnRSA.RandomPrime(16);
        Rsa.n = jnRSA.Multiply(Rsa.p, Rsa.q);
        Rsa.f = jnRSA.Multiply((Rsa.p - 1), (Rsa.q - 1));
        do {
            Rsa.e = jnRSA.g_Rnd.Random(65536);  //小于2^16，65536=2^16
            Rsa.e = Rsa.e | 1;   //保证最低位是1，即保证是奇数，因f一定是偶数，要互素，只能是奇数
        } while (jnRSA.SteinGcd(Rsa.e, Rsa.f) != 1);

        Rsa.d = jnRSA.Euclid(Rsa.e, Rsa.f);

        Rsa.s = 0;
        t = parseInt(Rsa.n / 2);
        while (t > 0) {
            Rsa.s++;   //s=log2(n)
            t = parseInt(t / 2);
        }

        return Rsa;
    };


    jnRSA.RSA_Init = function () {
        if (!jnRSA.g_Rnd) {
            jnRSA.g_Rnd = new jnRSA.RandNumber();
        }

        return jnRSA.RsaGetParam();
    };

    jnRSA.RSA_Encrypt_UInt8Array = function (rsa_d, rsa_n, src) {
         var result = '', tmpStr;

        var length = src.length;
        for (i = 0; i < length; i++) {
            tmpStr = jnRSA.PowMod(src[i], rsa_d, rsa_n);
            result += ('00000000' + tmpStr.toString(16)).substr(-8); /** 32位int转16进制, 补零*/
        }

        return result;
    };

	jnRSA.RSA_Encrypt_32UIntTo8Array = function (rsa_d, rsa_n, src) {
        var tmpStr;

        var length = src.length;

        var uint8arr = new Uint8Array(length * 4);

        for (i = 0; i < length; i++) {
            tmpStr = jnRSA.PowMod(src[i], rsa_d, rsa_n);

            uint8arr[i * 4 + 0] = (tmpStr & 0x000000ff);
            uint8arr[i * 4 + 1] = (tmpStr & 0x0000ff00) >> 8;
            uint8arr[i * 4 + 2] = (tmpStr & 0x00ff0000) >> 16;
            uint8arr[i * 4 + 3] = (tmpStr & 0xff000000) >> 24;

        }

        return uint8arr;
    };

    jnRSA.RSA_Decrypt_UInt8Array = function (rsa_e, rsa_n, src) {
        if (src.length % 8 != 0) {
            return null;
        }

        var length = src.length / 8;
        var uint8Arr = new Uint8Array(length);

        var tmpCharCode;
        for (i = 0; i < length; i++) {
            var tmpHex = parseInt(src.substr(i * 8, 8), 16);
            tmpCharCode = jnRSA.PowMod(tmpHex, rsa_e, rsa_n);

            uint8Arr[i] = tmpCharCode;
        }

        return uint8Arr;
    };
    
    jnRSA.RSA_Encrypt = function (rsa_d, rsa_n, src) {
        var result = '', tmpStr;

        var length = src.length;
        for (i = 0; i < length; i++) {
            tmpStr = jnRSA.PowMod(src.charCodeAt(i), rsa_d, rsa_n);
            result += ('00000000' + tmpStr.toString(16)).substr(-8); /** 32位int转16进制, 补零*/
        }

        return result;
    };

    jnRSA.RSA_Decrypt = function (rsa_e, rsa_n, src) {
        if (src.length % 8 != 0) {
            return null;
        }

        var length = src.length / 8;

        var result = '', tmpCharCode;
        for (i = 0; i < length; i++) {
            var tmpHex = parseInt(src.substr(i * 8, 8), 16);
            tmpCharCode = jnRSA.PowMod(tmpHex, rsa_e, rsa_n);

            result += String.fromCharCode(tmpCharCode);
        }

        return result;
    };

    jnRSA.Multiply = function (a, b) {
        var bita = biFromNumber(a);
        var bitb = biFromNumber(b);

        var mult = biMultiply(bita, bitb);

        return parseInt(biToDecimal(mult));
    };

    /* 模乘运算，返回值 x=a*b mod n  */
    jnRSA.MulMod = function (a/*Number*/, b/*Number*/, n/*Number*/) {
        var bita = biFromNumber(a);
        var bitb = biFromNumber(b);
        var bitn = biFromNumber(n);

        var mult = biMultiply(bita, bitb);
        var modu = biModulo(mult, bitn);

        return parseInt(biToDecimal(modu));
        //return a * b % n;
    };

    /* 模幂运算，返回值 x=base^pow mod n */
    jnRSA.PowMod = function (base/*Number*/, pow/*Number*/, n/*Number*/) {
        var a = base, b = pow, c = 1;
        while (b) {
            while (!(b & 1)) {
                b = parseInt(b / 2);  //a=a * a % n;    //函数看起来可以处理64位的整数，但由于这里a*a在a>=2^32时已经造成了溢出，因此实际处理范围没有64位
                a = jnRSA.MulMod(a, a, n);
            }
            b--;        //c=a * c % n;        //这里也会溢出，若把64位整数拆为两个32位整数不知是否可以解决这个问题。
            c = jnRSA.MulMod(a, c, n);
        }
        return c;
    };

})(jnRSA);









