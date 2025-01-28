package com.github.jleskovar.btcrpc

import com.googlecode.jsonrpc4j.JsonRpcMethod
import java.math.BigDecimal

/**
 * Created by james on 1/12/17.
 */
interface BitcoinRpcClient {

    @JsonRpcMethod("abandontransaction")
    fun abandonTransaction(transactionId: String)

    @JsonRpcMethod("abortrescan")
    fun abortRescan()

    @JsonRpcMethod("addmultisigaddress")
    fun addMultiSigAddress(required: Int? = null, keys: List<String>): String

    @JsonRpcMethod("addnode")
    fun addNode(address: String, operation: NodeListOperation)

    @JsonRpcMethod("backupwallet")
    fun backupWallet(destination: String)

    @JsonRpcMethod("clearbanned")
    fun clearBanned()

    @JsonRpcMethod("createmultisig")
    fun createMultiSig(required: Int, keys: List<String>): MultiSigAddress

    @JsonRpcMethod("createrawtransaction")
    fun createRawTransaction(
            inputs: List<OutPoint>,
            outputs: Map<String, BigDecimal>,
            lockTime: Int? = null,
            replaceable: Boolean? = null
    ): String

    @JsonRpcMethod("decoderawtransaction")
    fun decodeRawTransaction(transactionId: String): Transaction

    @JsonRpcMethod("decodescript")
    fun decodeScript(scriptHex: String): DecodedScript

    @JsonRpcMethod("disconnectnode")
    fun disconnectNode(nodeAddress: String? = null, nodeId: Int? = null)

    @JsonRpcMethod("dumpprivkey")
    fun dumpPrivateKey(address: String): String

    @JsonRpcMethod("dumpwallet")
    fun dumpWallet(filename: String): Map<*, *>

    @JsonRpcMethod("encryptwallet")
    fun encryptWallet(passphrase: String)

    @JsonRpcMethod("generate")
    fun generate(numberOfBlocks: Int, maxTries: Int? = null): List<String>

    @JsonRpcMethod("getaddednodeinfo")
    fun getAddedNodeInfo(): List<AddedNodeInfo>

    /**
     * Return information about the given bitcoin address.
     * Some of the information will only be present if the address is in the active wallet.
     */
    @JsonRpcMethod("getaddressinfo")
    fun getAddressInfo(
        /**
         * The bitcoin address for which to get information.
         */
        address: String
    ): BitcoinAddressInfo

    /**
     * Returns the list of addresses assigned the specified label.
     */
    @JsonRpcMethod("getaddressesbylabel")
    fun getAddressesByLabel(/** The label. */ label: String): Map<String, AddressByLabelResult>

    @JsonRpcMethod("getbalance")
    fun getBalance(
            account: String = "*",
            minconf: Int = 1,
            includeWatchOnly: Boolean = false): BigDecimal

    @JsonRpcMethod("getbestblockhash")
    fun getBestBlockhash(): String

    @JsonRpcMethod("getblock")
    fun getBlockData(blockHash: String, verbosity: Int = 0): String

    /**
     * If verbosity is 0, returns a string that is serialized, hex-encoded data for block 'hash'.
     * If verbosity is 1, returns an Object with information about block <hash>.
     * If verbosity is 2, returns an Object with information about block <hash>
     *     and information about each transaction.
     * If verbosity is 3, returns an Object with information about block <hash>
     *     and information about each transaction, including prevout information for inputs
     *     (only for unpruned blocks in the current best chain).
     */
    @JsonRpcMethod("getblock")
    fun getBlock(
        /**
         * The block hash
         */
        blockHash: String,
        /**
         * 0 for hex-encoded data,
         * 1 for a JSON object,
         * 2 for JSON object with transaction data,
         * and 3 for JSON object with transaction data including prevout information for inputs
         */
        verbosity: Int = 1): BlockInfo

    @JsonRpcMethod("getblock")
    fun getBlockWithTransactions(blockHash: String, verbosity: Int = 2): BlockInfoWithTransactions

    @JsonRpcMethod("getblockchaininfo")
    fun getBlockchainInfo(): BlockChainInfo

    @JsonRpcMethod("getblockcount")
    fun getBlockCount(): Int

    @JsonRpcMethod("getblockhash")
    fun getBlockHash(height: Int): String

    @JsonRpcMethod("getblockheader")
    fun getBlockHeader(blockHash: String, verbose: Boolean? = false): Any

    @JsonRpcMethod("getblocktemplate")
    fun getBlockTemplate(blockTemplateRequest: BlockTemplateRequest? = null)

    @JsonRpcMethod("getchaintips")
    fun getChainTips(): List<ChainTip>

    @JsonRpcMethod("getchaintxstats")
    fun getChainTransactionStats(
            blockWindowSize: Int? = null,
            blockHashEnd: String? = null
    ): ChainTransactionStats

    @JsonRpcMethod("getconnectioncount")
    fun getConnectionCount(): Int

    /**
     * Analyses a descriptor.
     * @param descriptor The descriptor (https://github.com/bitcoin/bitcoin/blob/master/doc/descriptors.md).
     */
    @JsonRpcMethod("getdescriptorinfo")
    fun getDescriptorInfo(descriptor: String): DescriptorInfo

    @JsonRpcMethod("getdifficulty")
    fun getDifficulty(): BigDecimal

    @JsonRpcMethod("getmemoryinfo")
    fun getMemoryInfo(mode: String? = null): Any

    @JsonRpcMethod("getmempoolancestors")
    fun getMempoolAncestors(transactionId: String): Any

    @JsonRpcMethod("getmempooldescendants")
    fun getMempoolDescendants(): Any

    @JsonRpcMethod("getmempoolentry")
    fun getMempoolEntry(transactionId: String): Map<*, *>

    @JsonRpcMethod("getmempoolinfo")
    fun getMempoolInfo(): MemPoolInfo

    @JsonRpcMethod("getmininginfo")
    fun getMiningInfo(): MiningInfo

    @JsonRpcMethod("getnettotals")
    fun getNetworkTotals(): NetworkTotals

    @JsonRpcMethod("getnetworkhashps")
    fun getNetworkHashesPerSeconds(lastBlocks: Int, height: Int): Long

    @JsonRpcMethod("getnetworkinfo")
    fun getNetworkInfo(): NetworkInfo

    /**
     * Returns a new Bitcoin address for receiving payments.
     * If 'label' is specified, it is added to the address book
     * so payments received with the address will be associated with 'label'.
     */
    @JsonRpcMethod("getnewaddress")
    fun getNewAddress(
        /**
         * The label name for the address to be linked to. It can also be set to the empty string "" to represent the default label.
         * The label does not need to exist, it will be created if there is no label by the given name.
         */
        label: String = "",
        /**
         * The address type to use. Options are "legacy", "p2sh-segwit", "bech32", and "bech32m".
         */
        address_type: String? = null
    ): String

    @JsonRpcMethod("getpeerinfo")
    fun getPeerInfo(): List<PeerInfo>

    @JsonRpcMethod("getrawchangeaddress")
    fun getRawChangeAddress(): String

    @JsonRpcMethod("getrawmempool")
    fun getRawMemPool(verbose: Boolean = false): List<Map<*, *>>

    @JsonRpcMethod("getrawtransaction")
    fun getRawTransaction(transactionId: String, verbosity: Int = 1): Transaction

    @JsonRpcMethod("getreceivedbyaddress")
    fun getReceivedByAddress(address: String, minConfirmations: Int = 1): BigDecimal

    @JsonRpcMethod("gettransaction")
    fun getWalletTransaction(transactionId: String): Map<*, *>

    @JsonRpcMethod("gettxout")
    fun getUnspentTransactionOutputInfo(transactionId: String, index: Int): Map<*, *>

    @JsonRpcMethod("gettxoutsetinfo")
    fun getUnspentTransactionOutputSetInfo(): UtxoSet

    @JsonRpcMethod("getwalletinfo")
    fun getWalletInfo(): Map<*, *>

    @JsonRpcMethod("importaddress")
    fun importAddress(
            scriptOrAddress: String,
            label: String? = null,
            rescan: Boolean? = null,
            includePayToScriptHash: Boolean? = null)

    /**
     * Import descriptors. This will trigger a rescan of the blockchain based on the earliest timestamp of all descriptors being imported.
     * Requires a new wallet backup.
     * When importing descriptors with multipath key expressions, if the multipath specifier contains exactly two elements,
     * the descriptor produced from the second elements will be imported as an internal descriptor.
     * Note: This call can take over an hour to complete if using an early timestamp; during that time, other rpc calls
     * may report that the imported keys, addresses or scripts exist but related transactions are still missing.
     * The rescan is significantly faster if block filters are available (using startup option "-blockfilterindex=1").
     * @link https://github.com/bitcoin/bitcoin/blob/master/src/wallet/rpc/backup.cpp#L1615
     */
    @JsonRpcMethod("importdescriptors")
    fun importDescriptors(request: List<ImportDescriptorsRequest>): List<ImportDescriptorsResult>

    @JsonRpcMethod("importprivkey")
    fun importPrivateKey(
            privateKey: String,
            label: String? = null,
            rescan: Boolean? = null
    )

    @JsonRpcMethod("importpubkey")
    fun importPublicKey(
            publicKey: String,
            label: String? = null,
            rescan: Boolean? = null
    )

    @JsonRpcMethod("createwallet")
    fun createWallet(
        /**
         * The name for the new wallet. If this is a path, the wallet will be created at the path location.
         */
        walletName: String,

        /**
         * Disable the possibility of private keys (only watchonlys are possible in this mode).
         */
        disablePrivateKeys: Boolean = false,

        /**
         * Create a blank wallet. A blank wallet has no keys or HD seed. One can be set using sethdseed.
         */
        blank: Boolean = false,

        /**
         * Encrypt the wallet with this passphrase.
         */
        passphrase: String? = null,

        /**
         * Keep track of coin reuse, and treat dirty and clean coins differently with privacy considerations in mind.
         */
        avoidReuse: Boolean = false,

        /**
         * Create a native descriptor wallet. The wallet will use descriptors internally to handle address creation
         */
        descriptors: Boolean = false,

        /**
         * Save wallet name to persistent settings and load on startup.
         * True to add wallet to startup list, false to remove, null to leave unchanged.
         */
        loadOnStartup: Boolean = false,

        /**
         * Use an external signer such as a hardware wallet. Requires -signer to be configured.
         * Wallet creation will fail if keys cannot be fetched. Requires disable_private_keys and descriptors set to true.
         */
        externalSigner: Boolean = false,
    ): CreateWalletResult

    @JsonRpcMethod("importwallet")
    fun importWallet(walletFile: String)

    @JsonRpcMethod("keypoolrefill")
    fun keypoolRefill(newSize: Int = 100)

    @JsonRpcMethod("listaddressgroupings")
    fun listAddressGroupings(): List<*>

    /**
     * List descriptors imported into a descriptor-enabled wallet.
     */
    @JsonRpcMethod("listdescriptors")
    fun listDescriptors(
        /**
         * Show private descriptors.
         */
        private: Boolean = false
    ): ListDescriptorsResult

    @JsonRpcMethod("listbanned")
    fun listBanned(): List<String>

    /**
     * Returns the list of all labels, or labels that are assigned to addresses with a specific purpose.
     * @return array of Label name
     */
    @JsonRpcMethod("listlabels")
    fun listLabels(
        /**
         * Address purpose to list labels for ('send','receive'). An empty string is the same as not providing this argument.
         */
        purpose: String? = null
    ): List<String>

    @JsonRpcMethod("listlockunspent")
    fun listLockUnspent(): List<Map<*, *>>

    @JsonRpcMethod("listreceivedbyaddress")
    fun listReceivedByAddress(
            minConfirmations: Int? = null,
            includeEmpty: Boolean? = null,
            includeWatchOnly: Boolean? = null
    ): List<Map<*, *>>

    @JsonRpcMethod("listsinceblock")
    fun listSinceBlock(
            blockHash: String? = null,
            targetConfirmations: Int? = null,
            includeWatchOnly: Boolean? = null,
            includeRemoved: Boolean? = null
    ): Map<*, *>

    @JsonRpcMethod("listtransactions")
    fun listTransactions(
            account: String? = null,
            count: Int? = null,
            skip: Int? = null,
            includeWatchOnly: Boolean? = null
    ): List<Map<*, *>>

    @JsonRpcMethod("listunspent")
    fun listUnspent(
            minConfirmations: Int? = null,
            maxConfirmations: Int? = null,
            addresses: List<String>? = null,
            includeUnsafe: Boolean? = null,
            queryOptions: QueryOptions? = null
    ): List<QueryResult>

    @JsonRpcMethod("listwallets")
    fun listWallets(): List<String>

    @JsonRpcMethod("lockunspent")
    fun lockUnspent(unlock: Boolean, unspentOutputs: List<OutPoint>): Boolean

    fun ping()

    @JsonRpcMethod("preciousblock")
    fun preciousBlock(block: String)

    @JsonRpcMethod("prioritisetransaction")
    fun prioritiseTransaction(transactionId: String, dummy: Int, feeDeltaSatoshis: Int)

    @JsonRpcMethod("pruneblockchain")
    fun pruneBlockchain(blockHeightOrUnixTimestamp: Long)

    @JsonRpcMethod("removeprunedfunds")
    fun removePrunedFunds(transactionId: String)

    @JsonRpcMethod("sendmany")
    fun sendMany(account: String,
                 addressAmounts: Map<String, BigDecimal>,
                 comment: String? = null,
                 subtractFee: Boolean = false,
                 replaceable: Boolean = false,
                 minConfirmations: Int? = null,
                 feeEstimateMode: FeeEstimateMode? = null)

    @JsonRpcMethod("sendrawtransaction")
    fun sendRawTransaction(transaction: String): String

    @JsonRpcMethod("sendtoaddress")
    fun sendToAddress(
            address: String,
            amount: BigDecimal,
            comment: String? = null,
            commentTo: String? = null,
            subtractFee: Boolean? = null,
            replaceable: Boolean? = null,
            minConfirmations: Int? = null,
            feeEstimateMode: FeeEstimateMode? = null): String

    @JsonRpcMethod("setban")
    fun setBan(
            address: String,
            operation: NodeListOperation,
            seconds: Int
    )

    @JsonRpcMethod("settxfee")
    fun setTransactionFee(fee: Double)

    @JsonRpcMethod("estimatesmartfee")
    fun estimateSmartFee(confTarget: Int, feeEstimateMode: FeeEstimateMode? = FeeEstimateMode.CONSERVATIVE): EstimateSmartFee

    @JsonRpcMethod("signmessage")
    fun signMessage(
            address: String,
            message: String
    )

    @JsonRpcMethod("signmessagewithprivkey")
    fun signMessageWithPrivateKey(
            privateKey: String,
            message: String
    )

    @JsonRpcMethod("signrawtransaction")
    fun signRawTransaction(transactionId: String)

    @JsonRpcMethod("submitblock")
    fun submitBlock(blockData: String)

    /**
     * The number of seconds that the server has been running
     */
    fun uptime(): Int

    @JsonRpcMethod("validateaddress")
    fun validateAddress(address: String)

    @JsonRpcMethod("verifychain")
    fun verifyChain(): Boolean

    @JsonRpcMethod("verifymessage")
    fun verifyMessage(
            address: String,
            signature: String,
            message: String
    )

    @JsonRpcMethod("searchrawtransactions")
    fun searchRawSerialisedTransactions(
            address: String,
            verbose: Int? = 0,
            skip: Int? = null,
            count: Int? = null,
            vInExtra: Int? = null,
            reverse: Boolean? = null): List<String>

    @JsonRpcMethod("searchrawtransactions")
    fun searchRawVerboseTransactions(
            address: String,
            verbose: Int? = 1,
            skip: Int? = null,
            count: Int? = null,
            vInExtra: Int? = null,
            reverse: Boolean? = null): List<SearchedTransactionResult>

    /**
     * btcd-specific extension methods
     */
    @JsonRpcMethod("authenticate")
    fun btcdAuthenticate(username: String, password: String)

    @JsonRpcMethod("generate")
    fun btcdGenerate(numberOfBlocks: Int): List<String>

    @JsonRpcMethod("getblock")
    fun btcdGetBlockWithTransactions(blockHash: String, verbose: Boolean = true): String

    @JsonRpcMethod("help")
    fun help(command: String = ""): String

    //<editor-fold desc="Wallet">

    /**
     * Stores the wallet decryption key in memory for 'timeout' seconds.
     * This is needed prior to performing transactions related to private keys such as sending bitcoins
     * Note:
     * Issuing the walletpassphrase command while the wallet is already unlocked will set a new unlock
     * time that overrides the old one.
     * @param passphrase - The wallet passphrase
     */
    @JsonRpcMethod("walletpassphrase")
    fun walletPassphrase(
        passphrase: String,
        timeout: Long, ///< The time to keep the decryption key in seconds; capped at 100000000 (~3 years).
    )
    //</editor-fold>
}
