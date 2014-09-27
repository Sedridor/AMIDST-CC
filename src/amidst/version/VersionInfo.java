package amidst.version;

/**
 * Information about what each supported version is
 */
public enum VersionInfo
{
    unknown(null),
    V1_8("wbuwrcnp[Lqt;sn[J[[Jry"),
    V1_7_10("riqinckb[Lmt;oi[J[[Jns"),
    V1_7_9("rhqhnbkb[Lms;oh[J[[Jnr"),
    V1_7_4("pzozmvjs[Lmm;lg[J[[J"),
    V1_7_2("pvovmsjp[Lmj;ld[J[[J"),
    V1_6_4("mvlv[Ljs;hn[J[J[J[J[J[[J"),
    V1_6_2("mulu[Ljr;hm[J[J[J[J[J[[J"),
    V1_6_1("msls[Ljp;hk[J[J[J[J[J[[J"),
    V1_5_2("[Bbdzbdrbawemabdsbfybdvngngbeuawfbgeawvawvaxrawbbfqausbjgaycawwaraavybkcavwbjubkila"),
    V1_5_1("[Bbeabdsbawemabdtbfzbdwngngbevawfbgfawvawvaxrawbbfrausbjhaycawwaraavybkdavwbjvbkila"),
    V1_4_7("[Baywayoaaszleaypbavaysmdazratabbaatqatqaulaswbanarnbdzauwatraohastbevasrbenbezbdmbdjkh"),
    V1_4_5("[Bayoaygaasrleayhbakaykmdazfassbapatjatjaueasobacarfbdoaupatkanzaslbekasjbecbenbdbbcykh"),
    V1_4_2("[Baxgawyaarjkpawzayyaxclnaxxarkazcasbasbaswargaytaqabcbathascamuardbcxarbbcpbdabbobbljy"),
    V1_3_2("[Batkatcaaofjbatdavbatgjwaubaogavfaovaovapnaocauwamxaxvapyaowajqanzayqanxayjaytaxkaxhik"),
    V1_3_1("adb"),
    V1_2_4("[Bkivmaftxdlvqacqcwfcaawnlnlvpjclrckqdaiyxgplhusdakagi[J[Jalfqabv"), // Includes 1.2.5
    V1_2_2("wl"),
    V1_1("[Bjsudadrvqluhaarcqevyzmqmqugiokzcepgagqvsonhhrgahqfy[J[Jaitpdbo"),
    V1_0("[Baesmmaijryafvdinqfdrzhabeabexexwadtnglkqdfagvkiahmhsadk[J[Jtkgkyu");

    public final String versionId;

    VersionInfo(String versionId)
    {
        this.versionId = versionId;
    }

    public boolean saveEnabled()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return super.toString().replace("_", ".").replace("V", "");
    }

    public boolean isAtLeast(VersionInfo other)
    {
        return this.ordinal() <= other.ordinal();
    }
}