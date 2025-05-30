package com.github.jleskovar.btcrpc

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

/**
 * Created by james on 4/12/17.
 * TBD generate from bitcoin.h
 */

enum class BlockTemplateRequestMode { template, proposal }

enum class FeeEstimateMode { UNSET, ECONOMICAL, CONSERVATIVE }

enum class NodeListOperation { onetry, add, remove }

data class OutPoint(
        val txid: String,
        val vout: Int)

typealias NumTime = Long
typealias StrAmount = String
typealias StrHex = String

fun NumTime.dis() = java.time.format.DateTimeFormatter.ISO_INSTANT
        .format(java.time.Instant.ofEpochSecond(this))

// https://github.com/bitcoin/bitcoin/blob/HEAD/src/rpc/blockchain.cpp#L1290
data class BlockChainInfo(
        val chain: String? = null,
        val blocks: Long? = null,
        val headers: Long? = null,
        val bestblockhash: String? = null,
        val bits: String? = null, ///< nBits: compact representation of the block difficulty target
        val target: String? = null, ///< The difficulty target
        val difficulty: BigDecimal? = null,
        val time: NumTime? = null, ///< The block time expressed in  UNIX_EPOCH_TIME
        val mediantime: NumTime? = null,
        val verificationprogress: Long? = null,
        val initialblockdownload: Boolean? = null, ///< (debug information) estimate of whether this node is in Initial Block Download mode
        val chainwork: String? = null,
        val size_on_disk: Long? = null, ///< the estimated size of the block and undo files on disk
        val pruned: Boolean? = null,
        val pruneheight: Long? = null,
        val automaticPruning: Boolean? = null, ///< whether automatic pruning is enabled (only present if pruning is enabled)
        val pruneTargetSize: Long? = null, ///< the target size used by pruning (only present if automatic pruning is enabled)
        val signetChallenge: String? = null, ///< the block challenge (aka. block script), in hexadecimal (only present if the current network is a signet)
        val softforks: List<SoftForkStatus>? = null,
        val bip9_softforks: Map<String, Bip9SoftForkStatus>? = null,
        val warnings: Any? = null, ///< any network and blockchain warnings (DEPRECATED) String | Array<String>

)

data class LockedMemoryInfo(
        val used: Int, ///< Number of bytes used
        val free: Int, ///< Number of bytes available in current arenas
        val total: Int, ///< Total number of bytes managed
        val locked: Int, ///< Amount of bytes that succeeded locking. If this number is smaller than total, locking pages failed at some point and key data could be swapped to disk."},
        val chunksUsed: Int, ///< Number allocated chunks
        val chunksFree: Int, ///< Number unused chunks
) {
        companion object {
                fun fromMap(m: Map<String, Int>) = LockedMemoryInfo(
                        m["used"]!!, m["free"]!!, m["total"]!!, m["locked"]!!,
                        m["chunks_used"]!!, m["chunks_free"]!!
                )
        }
}

data class SoftForkRejection(
        val status: Boolean? = null)

data class SoftForkStatus(
        val id: String? = null,
        val version: Long? = null,
        val reject: SoftForkRejection? = null)

data class Bip9SoftForkStatus(
        val status: String? = null,
        val bit: Long? = null,
        val startTime: Long? = null,
        val timeout: Long? = null,
        val since: Long? = null,
        val statistics: Bip9SoftForkStats? = null)

data class Bip9SoftForkStats(
        val period: Long? = null,
        val threshold: Long? = null,
        val elapsed: Long? = null,
        val count: Long? = null,
        val possible: Boolean? = null)

data class BlockTemplateRequest(
        val mode: BlockTemplateRequestMode? = null,
        val capabilities: List<String>? = null,
        val rules: List<String>? = null)

data class ChainTip(
        val height: Int? = null,
        val hash: String? = null,
        val branchlen: Int? = null,
        val status: String? = null)

data class ChainTransactionStats(
        val time: Long? = null,
        val txcount: Long? = null,
        val txrate: BigDecimal? = null)

data class MemPoolInfo(
        val size: Long? = null,
        val bytes: Long? = null,
        val usage: Long? = null,
        val maxmempool: Long? = null,
        val mempoolminfee: Long? = null
)

data class MiningInfo(
        val blocks: Long? = null,
        val currentblockweight: Long? = null,
        val currentblocktx: Long? = null,
        val difficulty: Long? = null,
        val errors: String? = null,
        val networkhashps: Long? = null,
        val pooledtx: Long? = null,
        val chain: String? = null
)

data class NetworkTotals(
        val totalbytesrecv: Long? = null,
        val totalbytessent: Long? = null,
        val timemillis: Long? = null,
        val uploadtarget: NetworkUploadTarget? = null)

data class NetworkUploadTarget(
        val timeframe: Long? = null,
        val target: Long? = null,
        val target_reached: Boolean? = null,
        val serve_historical_blocks: Boolean? = null,
        val bytes_left_in_cycle: Long? = null,
        val time_left_in_cycle: Long? = null)

data class NetworkInfo(
        val version: Long? = null,
        val subversion: String? = null,
        val protocolversion: Long? = null,
        val localservices: String? = null,
        val localrelay: Boolean? = null,
        val timeoffset: Int? = null,
        val connections: Int? = null,
        val networkactive: Boolean? = null,
        val networks: List<Network>? = null,
        val relayfee: BigDecimal? = null,
        val incrementalfee: BigDecimal? = null,
        val localaddresses: List<AddressInfo>? = null,
        val warnings: String? = null)

data class Network(
        val name: String? = null,
        val limited: Boolean? = null,
        val reachable: Boolean? = null,
        val proxy: String? = null,
        val proxy_randomize_credentials: Boolean? = null)

data class AddressInfo(
        val address: String,
        val port: Int,
        val score: Int)

/**
 * https://github.com/bitcoin/bitcoin/blob/master/src/wallet/rpc/addresses.cpp#L520
 */
data class BitcoinAddressInfo(
        val address: String, ///< The bitcoin address validated.
        val scriptPubKey: String, ///< The hex-encoded output script generated by the address.
        val ismine: Boolean, ///< If the address is yours.
        val iswatchonly: Boolean, ///< If the address is watchonly.
        val solvable: Boolean, ///< If we know how to spend coins sent to this address, ignoring the possible lack of private keys.
        val desc: String? = null, ///< A descriptor for spending coins sent to this address (only when solvable).
        val parent_desc: String? = null, ///< The descriptor used to derive this address if this is a descriptor wallet
        val isscript: Boolean? = null, ///< If the key is a script.
        val ischange: Boolean? = null, ///< If the address was used for change output.
        val iswitness: Boolean? = null, ///< If the address is a witness address.
        val witness_version: Int? = null, ///< The version number of the witness program.
        val witness_program: String? = null, ///< The hex value of the witness program.
        /**
         * The output script type. Only if isscript is true and the redeemscript is known.
         * Possible types: nonstandard, pubkey, pubkeyhash, scripthash, multisig, nulldata, witness_v0_keyhash
         * witness_v0_scripthash, witness_unknown.
         */
        val script: String? = null,
        val hex: String? = null, ///< The redeemscript for the p2sh address.
        val pubkeys: List<String>? = null, ///< Array of pubkeys associated with the known redeemscript (only if script is multisig).
        val sigsrequired: Int? = null, ///< The number of signatures required to spend multisig output (only if script is multisig).
        val pubkey: String? = null, ///< The hex value of the raw public key for single-key addresses (possibly embedded in P2SH or P2WSH).
        val embedded: Any? = null, ///< Information about the address embedded in P2SH or P2WSH, if relevant and known.
        /** Includes all getaddressinfo output fields for the embedded address, excluding metadata (timestamp, hdkeypath, hdseedid)
         * and relation to the wallet (ismine, iswatchonly). */
        val iscompressed: Boolean? = null, ///< If the pubkey is compressed.
        val timestamp: Long? = null, ///< The creation time of the key, if available, expressed in " + UNIX_EPOCH_TIME + "."
        val hdkeypath: String? = null, ///< The HD keypath, if the key is HD and available.
        val hdseedid: String? = null, ///< The Hash160 of the HD seed.
        val hdmasterfingerprint: String? = null, ///< The fingerprint of the master key.
        val labels: List</** Label name (defaults to ""). */ String> = emptyList(), ///< Array of labels associated with the address. Currently limited to one label but returned

)

/**
 * https://github.com/bitcoin/bitcoin/blob/master/src/rpc/output_script.cpp#L28
 */
data class ValidateAddressResult(
        val isvalid: Boolean, ///< If the address is valid or not
        val address: String? = null, ///< The bitcoin address validated
        val scriptPubKey: String? = null, ///< The hex-encoded output script generated by the address
        val isscript: Boolean? = null, ///< If the key is a script
        val iswitness: Boolean? = null, ///< If the address is a witness address
        val witness_version: Int? = null, ///< The version number of the witness program
        val witness_program: String? = null, ///< The hex value of the witness program
        val error: String? = null, ///< Error message, if any
        val error_locations: Array<Int>? = null, ///< Indices of likely error locations in address, if known (e.g. Bech32 errors)
        // index - index of a potential error
)

data class PeerInfo(
        val id: Long? = null,
        val addr: String? = null,
        val addrlocal: String? = null,
        val addrbind: String? = null,
        val services: String? = null,
        val relaytxes: Boolean? = null,
        val lastsend: Long? = null,
        val lastrecv: Long? = null,
        val bytessent: Long? = null,
        val bytesrecv: Long? = null,
        val conntime: Long? = null,
        val timeoffset: Long? = null,
        val pingtime: Double? = null,
        val minping: Double? = null,
        val version: Long? = null,
        val subver: String? = null,
        val inbound: Boolean? = null,
        val addnode: Boolean? = null,
        val startingheight: Long? = null,
        val banscore: Long? = null,
        val synced_headers: Long? = null,
        val synced_blocks: Long? = null,
        val inflight: List<*>? = null,
        val whitelisted: Boolean? = null,
        val bytessent_per_msg: BytesPerMessage? = null,
        val bytesrecv_per_msg: BytesPerMessage? = null)

data class BytesPerMessage(
        val alert: Long? = null,
        val filterload: Long? = null,
        val getheaders: Long? = null,
        val getblocks: Long? = null,
        val mempool: Long? = null,
        val ping: Long? = null,
        val pong: Long? = null,
        val verack: Long? = null,
        val inv: Long? = null,
        val version: Long? = null)

data class UtxoSet(
        val height: Int? = null,
        val bestblock: String? = null,
        val transactions: Int? = null,
        val txouts: Int? = null,
        val bogosize: Long? = null,
        val hash_serialized_2: String? = null,
        val disk_size: Long? = null,
        val total_amount: BigDecimal? = null)

data class QueryResult(
        val txid: String? = null,
        val vout: Int? = null,
        val label: String? = null,
        val address: String? = null,
        val scriptPubKey: String? = null,
        val amount: BigDecimal? = null,
        val confirmations: Int? = null,
        val redeemScript: String? = null,
        val spendable: Boolean? = null,
        val solvable: Boolean? = null,
        val safe: Boolean? = null)

data class QueryOptions(
        val minimumAmount: String? = null,
        val maximumAmount: String? = null,
        val maximumCount: String? = null,
        val minimumSumAmount: String? = null)

data class TransactionInput(
        val txid: String? = null,
        val vout: Long? = null,
        val scriptSig: ScriptSignature? = null,
        val txinwitness: List<String>? = null,
        val coinbase: String? = null,
        val sequence: Long? = null)

data class ScriptSignature(
        val asm: String? = null,
        val hex: String? = null)

data class TransactionOutput(
        val value: BigDecimal? = null,
        val n: Long? = null,
        val scriptPubKey: ScriptPubKey? = null)

data class ScriptPubKey(
        val asm: String? = null,
        val hex: String? = null,
        val reqSigs: Long? = null,
        val type: String? = null,
        val addresses: List<String>? = null)

data class Transaction(
        val txid: String? = null,
        val hash: String? = null,
        val size: Long? = null,
        val weight: Long? = null,
        val vsize: Long? = null,
        val version: Long? = null,
        val locktime: Long? = null,
        val vin: List<TransactionInput>? = null,
        val vout: List<TransactionOutput>? = null,
        val hex: String? = null,
        val blockhash: String? = null,
        val confirmations: Int? = null,
        val time: Long? = null,
        val blocktime: Long? = null
)

data class DecodedScript(
        val asm: String? = null,
        val hex: String? = null,
        val type: String? = null,
        val reqSigs: Int? = null,
        val addresses: List<String>? = null,
        val p2sh: String? = null)

data class MultiSigAddress(
        val address: String? = null,
        val redeemScript: String? = null)

data class AddedNodeInfo(
        val addednode: String? = null,
        val connected: Boolean? = null,
        val addresses: List<NodeAddress>? = null)

data class NodeAddress(
        val address: String? = null,
        val connected: String? = null)

data class BlockInfo(
        val hash: String? = null,
        val confirmations: Long? = null,
        val size: Long? = null,
        val strippedsize: Long? = null,
        val weight: Long? = null,
        val height: Long? = null,
        val version: Long? = null,
        val versionHex: String? = null,
        val merkleroot: String? = null,
        val tx: List<Any>? = null, // verbosity = 2 returns object
        val time: Long? = null,
        val mediantime: Long? = null,
        val nonce: Long? = null,
        val bits: String? = null,
        val target: String? = null, ///< The difficulty target
        val difficulty: BigDecimal? = null,
        val chainwork: String? = null,
        val nTx: Int? = null, ///< The number of transactions in the block
        val previousblockhash: String? = null,
        val nextblockhash: String? = null)

@JsonIgnoreProperties(ignoreUnknown = true)
data class BlockInfoWithTransactions(
        val hash: String? = null,
        val confirmations: Long? = null,
        val size: Long? = null,
        val strippedsize: Long? = null,
        val weight: Long? = null,
        val height: Long? = null,
        val version: Long? = null,
        val versionHex: String? = null,
        val merkleroot: String? = null,
        val tx: List<Transaction>? = null,
        val rawtx: List<Transaction>? = null,
        val time: Long? = null,
        val mediantime: Long? = null,
        val nonce: Long? = null,
        val bits: String? = null,
        val difficulty: BigDecimal? = null,
        val chainwork: String? = null,
        val previousblockhash: String? = null,
        val nextblockhash: String? = null
)


data class SearchedTransactionResult(
        val txid: String? = null,
        val hash: String? = null,
        val size: Long? = null,
        val locktime: Long? = null,
        val vin: List<TransactionInput>? = null,
        val vout: List<TransactionOutput>? = null,
        val hex: String? = null,
        val blockhash: String? = null,
        val confirmations: Int? = null,
        val time: Long? = null,
        val blocktime: Long? = null)

data class EstimateSmartFee(
        val feerate: BigDecimal? = null,
        val errors: List<String>? = null,
        val blocks: Long? = null
)

data class CreateWalletResult(
        val name: String? = null,
        val warning: String? = null
)

data class DescriptorInfo(
        val descriptor: String, ///< The descriptor in canonical form, without private keys. For a multipath descriptor, only the first will be returned.
        val multipath_expansion: List<String>?=null, ///< All descriptors produced by expanding multipath derivation elements. Only if the provided descriptor specifies multipath derivation elements.
        val checksum: String, ///< The checksum for the input descriptor
        val isrange: Boolean, ///< Whether the descriptor is ranged
        val issolvable: Boolean, ///< Whether the descriptor is solvable
        val hasprivatekeys: Boolean, ///< Whether the input descriptor contained at least one private key
)

// RANGE - Special type that is a NUM or [NUM,NUM]
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ImportDescriptorsRequest(
        val desc: String, ///< Descriptor to import.
        val active: Boolean = false, ///< Set this descriptor to be the active descriptor for the corresponding output type/externality
        val range: Any? = null, ///< If a ranged descriptor is used, this specifies the end or the range (in the form [begin,end]) to import
        val next_index: Long? = null, ///< If a ranged descriptor is set to active, this specifies the next index to generate addresses from
        /**
         * Time from which to start rescanning the blockchain for this descriptor, in UNIX_EPOCH_TIME
         * Use the string \"now\" to substitute the current synced blockchain time.
         * "now" can be specified to bypass scanning, for outputs which are known to never have been used, and
         * 0 can be specified to scan the entire blockchain. Blocks up to 2 hours before the earliest timestamp
         * of all descriptors being imported will be scanned as well as the mempool.
         */
        val timestamp: Long, ///<
        val internal: Boolean = false, ///< Whether matching outputs should be treated as not incoming payments (e.g. change)
        val label: String = "", ///< Label to assign to the address, only allowed with internal=false. Disabled for ranged descriptors
)

data class ListDescriptorsResult(
        val wallet_name: String, ///< Name of wallet this operation was performed on
        val descriptors: List<WalletDescriptorInfo> ///< Array of descriptor objects (sorted by descriptor string representation)
)

data class WalletDescriptorInfo(
        val desc: String, ///< Descriptor string representation
        val timestamp: Long, ///< The creation time of the descriptor
        val active: Boolean, ///< Whether this descriptor is currently used to generate new addresses
        val internal: Boolean? = null, ///< True if this descriptor is used to generate change addresses. False if this descriptor is used to generate receiving addresses; defined only for active descriptors
        // ranged
        val range: List<Int>? = null, ///< Defined only for ranged descriptors
                //val : RPCResult::Type::NUM, ///< Range start inclusive
                //val : RPCResult::Type::NUM, ///< Range end inclusive
        val next: Int? = null, ///< Same as next_index field. Kept for compatibility reason.
        val next_index: Int? = null ///<The next index to generate addresses from; defined only for ranged descriptors
)

/**
 * Response is an array with the same size as the input that has the execution result
 */
data class ImportDescriptorsResult(
        val success: Boolean,
        val warnings: List<String>? = null,
        val error: Any? = null ///< {RPCResult::Type::ELISION, "", "JSONRPC error"},
        //  ELISION,    //!< Special type to denote elision (...)
)

/** json object with information about address */
data class AddressByLabelResult(
        /** Purpose of address ("send" for sending address, "receive" for receiving address) */
        val purpose: String,
)

data class ReceivedByAddressResult(
        /** Only returns true if imported addresses were involved in transaction */
        val involvesWatchonly: Boolean,
        /** The receiving address */
        val address: String,
        /** The total amount in CURRENCY_UNIT received by the address */
        val amount: StrAmount,
        /** The number of confirmations of the most recent transaction included */
        val confirmations: Int,
        /** The label of the receiving address. The default label is "" */
        val label: String,
        /** The ids of transactions received with the address */
        val txids: List<String>
)

/** https://github.com/bitcoin/bitcoin/blob/master/src/wallet/rpc/transactions.cpp#L405 */
data class AddressTransactionDescription(
        /** The number of confirmations for the transaction. Negative confirmations means the
         * transaction conflicted that many blocks ago. */
        @field:JsonProperty
        val confirmations: Int,
        /** Only present if the transaction's only input is a coinbase one. */
        val generated: Boolean? = null,
        /**
         * Whether we consider the transaction to be trusted and safe to spend from.
         * Only present when the transaction has 0 confirmations (or negative confirmations, if conflicted).
         */
        val trusted: Boolean? = null,
        /** The block hash containing the transaction. */
        val blockhash: String? = null,
        /** The block height containing the transaction. */
        val blockheight: Long? = null,
        /** The index of the transaction in the block that includes it. */
        val blockindex: Int? = null,
        /** The block time expressed in UNIX_EPOCH_TIME. */
        val blocktime: NumTime,
        /** The transaction id. */
        val txid: StrHex,
        /** The hash of serialized transaction, including witness data. */
        val wtxid: StrHex,
        /** Confirmed transactions that have been detected by the wallet to conflict with this transaction. */
        val walletconflicts: List</** txid - The transaction id. */StrHex> = emptyList(),
        /** Only if 'category' is 'send'. The txid if this tx was replaced. */
        val replaced_by_txid: StrHex? = null,
        /** Only if 'category' is 'send'. The txid if this tx replaces another. */
        val replaces_txid: StrHex? = null,
        /** Transactions in the mempool that directly conflict with either this transaction or an ancestor transaction */
        val mempoolconflicts: List</** txid - The transaction id. */ StrHex> = emptyList(),
        /** If a comment to is associated with the transaction. */
        val to: String? = null,
        /** The transaction time expressed in UNIX_EPOCH_TIME. */
        val time: NumTime,
        /** The time received expressed in UNIX_EPOCH_TIME. */
        val timereceived: NumTime,
        /** If a comment is associated with the transaction, only present if not empty. */
        val comment: String? = null,
        /** ("yes|no|unknown") Whether this transaction signals BIP125 replaceability or has an unconfirmed ancestor signaling BIP125 replaceability.
         * May be unknown for unconfirmed transactions not in the mempool because their unconfirmed ancestors are unknown.
         */
        val `bip125-replaceable`: String,
        /** Only if 'category' is 'received'. List of parent descriptors for the output script of this coin. */
        val parent_descs: List</** The descriptor string. */String>? = null
)

data class AddressTransaction(
        /** Only returns true if imported addresses were involved in transaction. */
        val involvesWatchonly: Boolean? = null,
        /**  The bitcoin address of the transaction (not returned if the output does not have an address, e.g. OP_RETURN null data). */
        val address: String? = null,
        /**
         * The transaction category.
         *         "send"                  Transactions sent.
         *         "receive"               Non-coinbase transactions received.
         *         "generate"              Coinbase transactions received with more than 100 confirmations.
         *         "immature"              Coinbase transactions received with 100 or fewer confirmations.
         *         "orphan"                Orphaned coinbase transactions received.
         */
        val category: String,
        /** The amount in CURRENCY_UNIT. This is negative for the 'send' category, and is positive for all other categories */
        val amount: StrAmount,
        /** A comment for the address/transaction, if any */
        val label: String? = null,
        /** "the vout value" */
        val vout: Float,
        /** The amount of the fee in CURRENCY_UNIT. This is negative and only available for the
         * 'send' category of transactions. */
        val fee: StrAmount? = null,
        /** 'true' if the transaction has been abandoned (inputs are respendable). */
        val abandoned: Boolean,

        /// TBD remove copy-paste by mixins via delegates
        /** The number of confirmations for the transaction. Negative confirmations means the
         * transaction conflicted that many blocks ago. */
        val confirmations: Int,
        /** Only present if the transaction's only input is a coinbase one. */
        val generated: Boolean? = null,
        /**
         * Whether we consider the transaction to be trusted and safe to spend from.
         * Only present when the transaction has 0 confirmations (or negative confirmations, if conflicted).
         */
        val trusted: Boolean? = null,
        /** The block hash containing the transaction. */
        val blockhash: String? = null,
        /** The block height containing the transaction. */
        val blockheight: Long? = null,
        /** The index of the transaction in the block that includes it. */
        val blockindex: Int? = null,
        /** The block time expressed in UNIX_EPOCH_TIME. */
        val blocktime: NumTime,
        /** The transaction id. */
        val txid: StrHex,
        /** The hash of serialized transaction, including witness data. */
        val wtxid: StrHex,
        /** Confirmed transactions that have been detected by the wallet to conflict with this transaction. */
        val walletconflicts: List</** txid - The transaction id. */StrHex> = emptyList(),
        /** Only if 'category' is 'send'. The txid if this tx was replaced. */
        val replaced_by_txid: StrHex? = null,
        /** Only if 'category' is 'send'. The txid if this tx replaces another. */
        val replaces_txid: StrHex? = null,
        /** Transactions in the mempool that directly conflict with either this transaction or an ancestor transaction */
        val mempoolconflicts: List</** txid - The transaction id. */ StrHex> = emptyList(),
        /** If a comment to is associated with the transaction. */
        val to: String? = null,
        /** The transaction time expressed in UNIX_EPOCH_TIME. */
        val time: NumTime,
        /** The time received expressed in UNIX_EPOCH_TIME. */
        val timereceived: NumTime,
        /** If a comment is associated with the transaction, only present if not empty. */
        val comment: String? = null,
        /** ("yes|no|unknown") Whether this transaction signals BIP125 replaceability or has an unconfirmed ancestor signaling BIP125 replaceability.
         * May be unknown for unconfirmed transactions not in the mempool because their unconfirmed ancestors are unknown.
         */
        val `bip125-replaceable`: String,
        /** Only if 'category' is 'received'. List of parent descriptors for the output script of this coin. */
        val parent_descs: List</** The descriptor string. */String>? = null
)
